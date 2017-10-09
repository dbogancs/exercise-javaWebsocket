package cinema.service.impl;

import cinema.service.impl.Seat;
import cinema.service.impl.SeatStatus;

public class ExtendedSeat {

	private static int newId;
	
	private final int id;
	private Seat seat;
	private SeatStatus status;
	
	public ExtendedSeat(String row, String column) {
		initSeat(Integer.parseInt(row), Integer.parseInt(column));
		id = newId++;
	}
	
	public ExtendedSeat(int row, int column) {
		initSeat(row,column);
		id = newId++;
	}
	
	private void initSeat(int row, int column){

		seat = new Seat();
		seat.setRow(new Integer(row).toString());
		seat.setColumn(new Integer(column).toString());
		this.status = SeatStatus.FREE;
	}
	
	public int getId() {
		return id;
	}
	public int getColumn() {
		return Integer.parseInt(seat.getColumn());
	}
	public int getRow() {
		return Integer.parseInt(seat.getRow());
	}
	public SeatStatus getStatus() {
		return status;
	}
	public void setStatus(SeatStatus status) {
		this.status = status;
	}
	public Seat getSeat() {
		return seat;
	}

	
	
}
