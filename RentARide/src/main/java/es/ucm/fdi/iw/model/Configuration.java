package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.NamedQuery;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name="Configuration.findAll",
            query="SELECT c FROM Configuration c")

public class Configuration {

        @Id
        @Column(nullable = false)
        private String configKey;

        @Column(nullable = false)
        private String configValue;

}