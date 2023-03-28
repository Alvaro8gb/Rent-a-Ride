package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Booking;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *  User management.
 *
 *  Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
public class UserController {

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
    private LocalData localData;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final String mensajeErrorImagen = "La imagen proporcionada no es válida";
	private final String mensajeErrorDNI = "El DNI proporcionado no es válido";
	private final String mensajeErrorEmail = "El correo proporcionado no es válido";
	private final String mensajeErrorPassword = "Las contraseñas no coinciden o no cumple los requisitos";

    /**
     * Exception to use when denying access to unauthorized users.
     * 
     * In general, admins are always authorized, but users cannot modify
     * each other's profiles.
     */
	@ResponseStatus(
		value=HttpStatus.FORBIDDEN, 
		reason="No eres administrador, y éste no es tu perfil")  // 403
	public static class NoEsTuPerfilException extends RuntimeException {}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 * for example, a possible encoding of "test" is 
	 * {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

    /**
     * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
     * @param byteLength
     * @return
     */
    public static String generateRandomBase64Token(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token); //base64 encoding
    }

    /**
     * Landing page for a user profile
     */
	@GetMapping("{id}")
    public String index(@PathVariable long id, Model model, HttpSession session) {
        User target = entityManager.find(User.class, id);
        model.addAttribute("user", target);
        return "user";
    }

    /**
     * Alter or create a user
     */
	@PostMapping("/{id}")
	@Transactional
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id, 
			@ModelAttribute User edited, 
			@RequestParam(required=false) String pass2,
			Model model, HttpSession session) throws IOException {

        User requester = (User)session.getAttribute("u");
        User target = null;
        if (id == -1 && requester.hasRole(Role.ADMIN)) {
            // create new user with random password
            target = new User();
            target.setPassword(encodePassword(generateRandomBase64Token(12)));
            target.setEnabled(true);
            entityManager.persist(target);
            entityManager.flush(); // forces DB to add user & assign valid id
            id = target.getId();   // retrieve assigned id from DB
        }
        
        // retrieve requested user
        target = entityManager.find(User.class, id);
        model.addAttribute("user", target);
		
		if (requester.getId() != target.getId() &&
				! requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}
		
		if (edited.getPassword() != null) {
            if ( ! edited.getPassword().equals(pass2)) {
                // FIXME: complain
            } else {
                // save encoded version of password
                target.setPassword(encodePassword(edited.getPassword()));
            }
		}		
		target.setUsername(edited.getUsername());
		target.setFirstName(edited.getFirstName());
		target.setLastName(edited.getLastName());

		// update user session so that changes are persisted in the session, too
        if (requester.getId() == target.getId()) {
            session.setAttribute("u", target);
        }

		return "user";
	}	

    /**
     * Returns the default profile pic
     * 
     * @return
     */
    private static InputStream defaultPic() {
	    return new BufferedInputStream(Objects.requireNonNull(
            UserController.class.getClassLoader().getResourceAsStream(
                "static/img/default-pic.png")));
    }

    /**
     * Downloads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        File f = localData.getFile("user", ""+id+".jpg");
        InputStream in = new BufferedInputStream(f.exists() ?
            new FileInputStream(f) : UserController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }

    /**
     * Uploads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("{id}/pic")
	@ResponseBody
    public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id, 
        HttpServletResponse response, HttpSession session, Model model) throws IOException {

        User target = entityManager.find(User.class, id);
        model.addAttribute("user", target);
		
		// check permissions
		User requester = (User)session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				! requester.hasRole(Role.ADMIN)) {
            throw new NoEsTuPerfilException();
		}
		
		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", ""+id+".jpg");
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
                log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "{\"status\":\"photo uploaded correctly\"}";
    }
    
    /**
     * Returns JSON with all received messages
     */
    @GetMapping(path = "received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody  // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Message.Transfer> retrieveMessages(HttpSession session) {
		long userId = ((User)session.getAttribute("u")).getId();		
		User u = entityManager.find(User.class, userId);
		log.info("Generating message list for user {} ({} messages)", 
				u.getUsername(), u.getReceived().size());
		return  u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}	
    
    /**
     * Returns JSON with count of unread messages 
     */
	@GetMapping(path = "unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		long userId = ((User)session.getAttribute("u")).getId();		
		long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
			.setParameter("userId", userId)
			.getSingleResult();
		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
    }
    
    /**
     * Posts a message to a user.
     * @param id of target user (source user is from ID)
     * @param o JSON-ized message, similar to {"message": "text goes here"}
     * @throws JsonProcessingException
     */
    @PostMapping("/{id}/msg")
	@ResponseBody
	@Transactional
	public String postMsg(@PathVariable long id, 
			@RequestBody JsonNode o, Model model, HttpSession session) 
		throws JsonProcessingException {
		
		String text = o.get("message").asText();
		User u = entityManager.find(User.class, id);
		User sender = entityManager.find(
				User.class, ((User)session.getAttribute("u")).getId());
		model.addAttribute("user", u);
		
		// construye mensaje, lo guarda en BD
		Message m = new Message();
		m.setRecipient(u);
		m.setSender(sender);
		m.setDateSent(LocalDateTime.now());
		m.setText(text);
		entityManager.persist(m);
		entityManager.flush(); // to get Id before commit
		
		ObjectMapper mapper = new ObjectMapper();
		/*
		// construye json: método manual
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("from", sender.getUsername());
		rootNode.put("to", u.getUsername());
		rootNode.put("text", text);
		rootNode.put("id", m.getId());
		String json = mapper.writeValueAsString(rootNode);
		*/
		// persiste objeto a json usando Jackson
		String json = mapper.writeValueAsString(m.toTransfer());

		log.info("Sending a message to {} with contents '{}'", id, json);

		messagingTemplate.convertAndSend("/user/"+u.getUsername()+"/queue/updates", json);
		return "{\"result\": \"message sent.\"}";
	}

	@GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
		User u = (User)session.getAttribute("u");
		model.addAttribute("user", u);
        return "profile";
    }

	@PostMapping("/{id}/delete")
    @Transactional
    public String delete(@PathVariable long id, RedirectAttributes redirAttrs, HttpSession session){
        User requester = (User)session.getAttribute("u");
        User user = entityManager.find(User.class, requester.getId());
        String roles = user.getRoles();
         //Comprobar si roll admin
		Boolean reservaActiva = true;
        if(roles.contains("ADMIN")){
            User target = entityManager.find(User.class, id);
			if(target.getBookings().size() > 0){
				LocalDate today = LocalDate.now();
				
				for(int i = 0; i < target.getBookings().size(); i++){
					if(target.getBookings().get(i).getId().getOut_date().compareTo(today) > 0){
						reservaActiva = true;
						System.out.println("tiene una reserva activa XDFD");
					}
				}
			}
			if(target != null && id != user.getId() && !reservaActiva){
				target.setEnabled(false);
            	entityManager.persist(target);
				redirAttrs.addFlashAttribute("successMessage", "El usuario se ha eliminado con éxito");
			}
			else{
				redirAttrs.addFlashAttribute("errorMessage", "La operación ha fracasado");
			}
            
        }
        return "redirect:/userList";
    }

	@PostMapping("/{id}/modify")
	@Transactional
	public String specificUserMod(RedirectAttributes redirAttrs,
									@PathVariable long id,
									@RequestParam("dni_gestion_u") String dni,
									@RequestParam("correo_gestion_u") String correo,
									@RequestParam("apellido_gestion_u") String apellido,
									@RequestParam("nombre_gestion_u") String nombre,
									@RequestParam("usuario_gestion_u") String cuenta,
									@RequestParam("roles_gestion_u") String roles,
									@RequestParam("imagen_gestion_u") MultipartFile imagen,  
									HttpSession session){
		String err = checkData(dni, correo, imagen);
		if(err == null){
			try {
				User requester = (User)session.getAttribute("u");
				User target = entityManager.find(User.class, id);
				if(requester.getRoles().toUpperCase().contains("ADMIN")){
					User aux = entityManager.createNamedQuery("User.byUserName", User.class).setParameter("username", cuenta).getSingleResult();
					if(!cuenta.isEmpty() && aux == null){
						target.setUsername(cuenta);
						if(!imagen.isEmpty()){
							File f = localData.getFile("user", target.getId()+".jpg");
							imagen.transferTo(new File(f.getAbsolutePath()));
							target.setImagePath(target.getId()+".jpg");
						}
						if(!dni.isEmpty()){
							target.setDNI(dni);
						}
						if(!correo.isEmpty()){
							target.setEmail(correo);
						}
						if(!apellido.isEmpty()){
							target.setLastName(apellido);
						}
						if(!nombre.isEmpty()){
							target.setFirstName(nombre);
						}
						if(!roles.isEmpty()){
							target.setRoles(roles);
						}
						if (requester.getId() == target.getId()) {
							session.setAttribute("u", target);
						}
						redirAttrs.addFlashAttribute("successMessage", "El Usuario " + id + " se ha modificado con éxito");
					}
					else{
						redirAttrs.addFlashAttribute("errorMessage", "El Nombre de usuario '" + cuenta + "' ya existe. Por favor, modifícalo");
					}
					
				}
				else{
					redirAttrs.addFlashAttribute("errorMessage", "No tienes permiso para realizar la acción");
				}

			} catch (Exception e) {
				redirAttrs.addFlashAttribute("errorMessage", "La operación ha fracasado");
			}

		}
		else{
			redirAttrs.addFlashAttribute("errorMessage", err);
		}
		return "redirect:/userList";
	}

	@PostMapping("/profile/modify")
	@Transactional
	public String profileMod(RedirectAttributes redirAttrs,
								@RequestParam("dni_perfil") String dni,
								@RequestParam("correo_perfil") String correo,
								@RequestParam("apellido_perfil") String apellido,
								@RequestParam("nombre_perfil") String nombre,
								@RequestParam("imagen_perfil") MultipartFile imagen,  
								HttpSession session){
		String err = checkData(dni, correo, imagen);
		if(err == null){
			try{
				User requester = (User)session.getAttribute("u");
				User target = entityManager.find(User.class, requester.getId());
				if(!imagen.isEmpty()){
					File f = localData.getFile("user", target.getId()+".jpg");
					imagen.transferTo(new File(f.getAbsolutePath()));
					target.setImagePath(target.getId()+".jpg");
				}
				if(!dni.isEmpty()){
					target.setDNI(dni);
				}
				if(!correo.isEmpty()){
					target.setEmail(correo);
				}
				if(!apellido.isEmpty()){
					target.setLastName(apellido);
				}
				if(!nombre.isEmpty()){
					target.setFirstName(nombre);
				}
				session.setAttribute("u", target);
				redirAttrs.addFlashAttribute("successMessage", "El perfil se ha modificado con éxito");
			} catch(Exception e) {
				redirAttrs.addFlashAttribute("errorMessage", "La operación ha fracasado");
			}
		} else {
			redirAttrs.addFlashAttribute("errorMessage", err);
		}							
		return "redirect:/user/profile";
	}

	@GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

	@PostMapping("/signup")
	@Transactional
	public String signup(RedirectAttributes redirAttrs,
						@RequestParam("dni") String dni,
						@RequestParam("correo") String email,
						@RequestParam("primer_apellido") String lastName,
						@RequestParam("segundo_apellido") String lastName1,
						@RequestParam("nombre") String firstName,
						@RequestParam("usuario") String username,
						@RequestParam("password") String pass,
						@RequestParam("conf_pass") String conf_pass,
						HttpSession session) {

		User newUser = new User();
		
		Pattern patternDNI = Pattern.compile("^\\d{8}[a-zA-Z]$");
		Matcher matcherDNI = patternDNI.matcher(dni);
		if (matcherDNI.matches()) {
			Pattern patternEmail = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
			Matcher matcherEmail = patternEmail.matcher(email);
			if (matcherEmail.matches()) {
				Pattern patternPass = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,16}$");
				Matcher matcherPass = patternPass.matcher(pass);
				if (matcherPass.matches()) {
					if (pass.equals(conf_pass)) {
						newUser.setDNI(dni);
						newUser.setEmail(email);
						newUser.setLastName(lastName + " " + lastName1);
						newUser.setFirstName(firstName);
						newUser.setUsername(username);
						newUser.setPassword(encodePassword(pass));
						newUser.setImagePath("default-pic.png");
						newUser.setRoles("USER");
						newUser.setEnabled(true);
						entityManager.persist(newUser);
						entityManager.flush();
						redirAttrs.addFlashAttribute("successMessage", "Usuario registrado con éxito");
					}
					
				} else
					redirAttrs.addFlashAttribute("errorMessage", mensajeErrorPassword);
			} else
				redirAttrs.addFlashAttribute("errorMessage", mensajeErrorEmail);
		} else 
			redirAttrs.addFlashAttribute("errorMessage", mensajeErrorDNI);

		return "redirect:/user/signup";
	}

	private String checkData(String dni, String correo, MultipartFile imagen){
		Pattern patternDNI = Pattern.compile("^\\d{8}[a-zA-Z]$");
		Matcher matcherDNI = patternDNI.matcher(dni);
		if(matcherDNI.matches()){
			Pattern patternEmail = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
			Matcher matcherEmail = patternEmail.matcher(correo);
			if(matcherEmail.matches()){
				try {
					if(!(imagen == null || imagen.isEmpty())){
						BufferedImage image = ImageIO.read(imagen.getInputStream());
						if (image == null) {
							return mensajeErrorImagen;
						}
			
						String format = imagen.getContentType();
						if (format == null || !format.toLowerCase().startsWith("image/")) {
							return mensajeErrorImagen;
						}
					}		
					return null;

				} catch (IOException e) {
					return mensajeErrorImagen;
				}
			}
			else{
				return mensajeErrorEmail;
			}
		}
		else{
			return mensajeErrorDNI;
		}
	}

}
