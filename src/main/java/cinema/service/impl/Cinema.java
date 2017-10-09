package cinema.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import cinema.service.impl.ArrayOfSeat;

import cinema.service.impl.Seat;
import cinema.service.impl.SeatStatus;


public class Cinema {

	private static int rows;
	private static int columns;
	private static ArrayList<ExtendedSeat> allSeats = new ArrayList<>();
	private static ArrayList<LockedExtendedSeats> allLocks = new ArrayList<>();
	
	
	private static ExtendedSeat getSeat(String row, String column) throws Exception{
		int irow = Integer.parseInt(row);
		int icolumn = Integer.parseInt(column);
		
		for (int i = 0; i < allSeats.size(); i++) {
					
			ExtendedSeat target = allSeats.get(i);
			if(target.getColumn() == icolumn
					&& target.getRow() == irow)
				return target;
		}
		throw new Exception("Seat with row "+row+" and column "+column
				+" is not found in the list of seats with "+allSeats.size()+" elements.");
	}
	
	private static LockedExtendedSeats getLock(String id) throws Exception {
		for (int i = 0; i < allLocks.size(); i++) {
			LockedExtendedSeats lock = allLocks.get(i);
			if(lock.getId().equals(id))
				return lock;
		}
		throw new Exception("Lock with id "+id+" is not found in the list of locks with "+allSeats.size()+" elements.");
	}
		
	public static void initRoom(int rows, int columns) throws Exception {
		
		allSeats = new ArrayList<ExtendedSeat>();
		allLocks = new ArrayList<LockedExtendedSeats>();
		setRows(rows);
		setColumns(columns);
		
		if(rows < 1 || columns < 1) throw new Exception("The number of rows and columns must be more than null!");
		
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				
				int trueRow;
				try {
					trueRow = row+1;
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
					throw new Exception("The number of rows must be between 1-26.");
				}
				int trueColumn = column+1;
				
				ExtendedSeat seat = new ExtendedSeat(trueRow,trueColumn);
				seat.setStatus(SeatStatus.FREE);
				allSeats.add(seat);

				System.out.println("Seat added: row " + trueRow + ", column " + trueColumn);
			}
		}
		System.out.println("All seats are added! Sum: " + allSeats.size());
	}

	public static ArrayOfSeat getSeatArray() throws Exception {
		ExtendedArrayOfSeat aos = new ExtendedArrayOfSeat();
		List<Seat> seats = new ArrayList<>();
		for(int i = 0; i < allSeats.size(); i++) {
			seats.add(allSeats.get(i).getSeat());
		}
		aos.setSeat(seats);
		return (ArrayOfSeat) aos;
	}
	
	public static ArrayList<ExtendedSeat> getAllSeats() throws Exception {
		return allSeats;
	}

	public static SeatStatus getSeatStatus(Seat seat) throws Exception {
		try{
			return getSeat(seat.getRow(),seat.getColumn()).getStatus();
		}
		catch(Exception e){
			System.out.println("Exception: " + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public static String lock(Seat seat, int count) throws Exception {

		ArrayList<ExtendedSeat> seats = new ArrayList<>();
		LockedExtendedSeats locked = new LockedExtendedSeats();  
		
		try {			
			for (int i = 0; i < count; i++) {
				String targetColumn = new Integer(Integer.parseInt(seat.getColumn()) + i).toString();
				ExtendedSeat targetSeat = getSeat(seat.getRow(), targetColumn);
				
				if(!targetSeat.getStatus().value().equals(SeatStatus.FREE.value()))
					throw new Exception("The seat " + seat.getRow() + " " + targetColumn
							+ " is not " + SeatStatus.FREE.value()
							+ ". The status is " + targetSeat.getStatus().value());
				
				if(targetSeat != null) seats.add(targetSeat);
			}
			locked.setSeats(seats);
			locked.lockAll();
			allLocks.add(locked);
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new Exception(e.getMessage());
		}
		return locked.getId();
	}

	public static ExtendedSeat unlock(String lockId) throws Exception {
		
		LockedExtendedSeats lock = null;
		ExtendedSeat seat = null;
		
		try {
			lock = getLock(lockId);
			lock.unlockAll();
			seat = lock.getSeats().get(0);
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new Exception(e.getMessage());
		}
		
		return seat;
	}

	public static ExtendedSeat reserve(String lockId) throws Exception {

		LockedExtendedSeats lock = null;
		ExtendedSeat seat = null;
		
		try {
			lock = getLock(lockId);
			lock.reserveAll();
			seat = lock.getSeats().get(0);
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new Exception(e.getMessage());
		}
		
		return seat;
	}

	public static void buy(String lockId) throws Exception {
		try {
			LockedExtendedSeats lock = getLock(lockId);
			lock.buyAll();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public static int getRows() {
		return rows;
	}

	public static void setRows(int rows) {
		Cinema.rows = rows;
	}

	public static int getColumns() {
		return columns;
	}

	public static void setColumns(int columns) {
		Cinema.columns = columns;
	}

}
