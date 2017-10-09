package cinema.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import cinema.service.impl.Cinema;
import cinema.service.impl.ExtendedSeat;
import cinema.service.impl.Seat;
import cinema.service.impl.SeatStatus;

@ServerEndpoint("/cinema")
public class CinemaEndpoint {
	
	private static ArrayList<Session> sessions = new ArrayList<>();
	
	@OnOpen
	public void open(Session session) {
		sessions.add(session);
		System.out.println("WebSocket open socket");
	}

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
		System.out.println("WebSocket close socket");
	}

	@OnError
	public void error(Throwable t) {
		System.out.println("WebSocket error: " + t.getMessage());
	}

	@OnMessage
	public String message(String msg, Session session) {
		System.out.println("WebSocket message: " + msg);
				
		JsonReader jsonReader = Json.createReader(new StringReader(msg));
		JsonObject jsonObject = jsonReader.readObject();
		
		try{
			switch(jsonObject.getString("type")){
				case "initRoom":
					initRoom(jsonObject);
					break;
				case "getRoomSize":
					sendRoomSize(session);
					break;
				case "updateSeats":
					updateSeats(session);
					break;
				case "lockSeat":
					ExtendedSeat seat = new ExtendedSeat(jsonObject.getInt("row"), jsonObject.getInt("column"));
					lockSeat(session, seat);
					break;
				case "unlockSeat":
					unlockSeat(session, jsonObject.getString("lockId"));
					break;
				case "reserveSeat":
					reserveSeat(session, jsonObject.getString("lockId"));
					break;
				default:
			}
		} catch(Exception e){
			try {
				sendError(session, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	void initRoom(JsonObject initRoom) throws Exception{
		Cinema.initRoom(Integer.parseInt(initRoom.getString("rows")),
				Integer.parseInt(initRoom.getString("columns")));
	}
	
	void updateSeats(Session session) throws Exception{
    	for (ExtendedSeat seat : Cinema.getAllSeats()) {
    		sendSeatStatus(session, seat);
		}
	}
	
	void lockSeat(Session session, ExtendedSeat seat) throws Exception{
		Seat s = seat.getSeat();
		String lockId = Cinema.lock(s, 1);
		seat.setStatus(SeatStatus.LOCKED);
		sendLockId(session, lockId);
		sendSeatStatusForAll(seat);
	}

	void unlockSeat(Session session, String lockId) throws Exception{
		ExtendedSeat seat = Cinema.unlock(lockId);
		//sendLockId(session, lockId);
		sendSeatStatusForAll(seat);
	}
	
	void reserveSeat(Session session, String lockId) throws Exception{
		ExtendedSeat seat = Cinema.reserve(lockId);
		sendLockId(session, lockId);
		sendSeatStatusForAll(seat);
	}

	
	void sendRoomSize(Session session) throws IOException{
		JsonObject roomSizeObj = Json.createObjectBuilder()
				.add("type", "roomSize")
				.add("rows", Cinema.getRows())
				.add("columns", Cinema.getColumns())
				.build();
		String jsonStringRoomSize = roomSizeObj.toString();
		session.getBasicRemote().sendText(jsonStringRoomSize);
    }
	
	void sendSeatStatus(Session session, ExtendedSeat seat) throws IOException{

		JsonObject jsonSeat = Json.createObjectBuilder()
				.add("type", "seatStatus")
				.add("row", seat.getRow())
				.add("column", seat.getColumn())
				.add("status", seat.getStatus().value())
				.build();
		String jsonString = jsonSeat.toString();
		session.getBasicRemote().sendText(jsonString);
	}
	
	void sendLockId(Session session, String lockId) throws IOException{
		JsonObject jsonLockId = Json.createObjectBuilder()
				.add("type", "lockResult")
				.add("lockId", lockId)
				.build();
		String jsonStringLockId = jsonLockId.toString();
		session.getBasicRemote().sendText(jsonStringLockId);
	}
	
	void sendError(Session session, String msg) throws IOException{
		JsonObject jsonError = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", msg)
				.build();
		String jsonStringError = jsonError.toString();
		session.getBasicRemote().sendText(jsonStringError);
	}
	
	void sendSeatStatusForAll(ExtendedSeat seat) throws Exception{
		for (Session session : sessions) {
			sendSeatStatus(session, seat);
		}
	}
}