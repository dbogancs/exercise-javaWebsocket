
package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



public class ArrayOfSeat {

    @XmlElement(name = "Seat", nillable = true)
    protected List<Seat> seat;

    public List<Seat> getSeat() {
        if (seat == null) {
            seat = new ArrayList<Seat>();
        }
        return this.seat;
    }

}
