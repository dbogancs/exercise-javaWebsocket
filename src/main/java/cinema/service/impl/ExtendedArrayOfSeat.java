package cinema.service.impl;

import java.util.List;

import cinema.service.impl.ArrayOfSeat;
import cinema.service.impl.Seat;

public class ExtendedArrayOfSeat extends ArrayOfSeat {
	public void setSeat(List<Seat> s) {
		this.seat = s; 
	}
}
