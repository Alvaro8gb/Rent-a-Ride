package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
@Table(name = "Coche")
public class Coche {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", unique=true, nullable=false)
    private int ID;

    
}
