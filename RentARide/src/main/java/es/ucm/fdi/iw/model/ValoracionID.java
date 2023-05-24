package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Embeddable
@AllArgsConstructor
public class ValoracionID implements Serializable, Transferable<ValoracionID.Transfer> {
    
    private long vehicleID;
    private long userID;

    public ValoracionID(){
        
    }
    @Getter
    @AllArgsConstructor
	public static class Transfer {
        private long vehicleID;
        private long userID;

    }

    @Override
    public Transfer toTransfer() {
       return new Transfer(vehicleID, userID);
    }
}
