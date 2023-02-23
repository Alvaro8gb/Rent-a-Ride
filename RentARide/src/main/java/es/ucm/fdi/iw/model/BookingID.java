package es.ucm.fdi.iw.model;

import lombok.Data;

import javax.persistence.Embeddable;

import java.io.Serializable;

@Data
@Embeddable
public class BookingID implements Serializable {

    private long vehicleID;
    private long userID;

}