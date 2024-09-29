package telran.net.games;

import org.json.JSONObject;
import telran.net.Request;
import telran.net.TcpClient;
import telran.net.games.model.GameGamerDto;
import telran.net.games.model.GamerDto;
import telran.net.games.model.MoveData;
import telran.net.games.model.SequenceGameGamerDto;
import telran.net.games.service.BullsCowsService;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BullsCowsProxy implements BullsCowsService {
    TcpClient tcpClient;

    public BullsCowsProxy(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public long createGame() {
        String response = tcpClient.sendAndReceive(new Request("createGame", ""));
        return Long.parseLong(response);
    }

    @Override
    public List<String> startGame(long gameId) {
        String response = tcpClient.sendAndReceive(new Request("startGame",Long.toString(gameId)));
        return List.of(response.split(";"));
    }

    @Override
    public void registerGamer(String username, LocalDate birthDate) {
        tcpClient.sendAndReceive
                (new Request("registerGamer",new GamerDto(username,birthDate).toString()));
    }

    @Override
    public void gamerJoinGame(long gameId, String username) {
        tcpClient.sendAndReceive
                (new Request("gamerJoinGame",new GameGamerDto(gameId,username).toString()));
    }

    @Override
    public List<Long> getNotStartedGames() {
        String response =
                tcpClient.sendAndReceive(new Request("getNotStartedGames",""));
        return fromJson(response);
    }

    @Override
    public List<MoveData> moveProcessing(String sequence, long gameId, String username) {
        String response =
                tcpClient.sendAndReceive
                        (new Request("moveProcessing",new SequenceGameGamerDto(sequence,gameId,username).toString()));
        return Stream.of(response.split(";"))
                .map(str -> new MoveData(new JSONObject(str)))
                .toList();
    }

    @Override
    public boolean gameOver(long gameId) {
        String response =
                tcpClient.sendAndReceive(new Request("gameOver",Long.toString(gameId)));
        return Boolean.parseBoolean(response);
    }

    @Override
    public List<String> getGameGamers(long gameId) {
        String response =
                tcpClient.sendAndReceive(new Request("getGameGamers",Long.toString(gameId)));
        return List.of(response.split(";"));
    }

	@Override
	public List<Long> getNotStartedGamesWithGamer(String username) {
		String response = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithGamer", username));
		return fromJson(response);
	}

	@Override
	public List<Long> getNotStartedGamesWithNoGamer(String username) {
		String response = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithNoGamer", username));
		return fromJson(response);
	}

	private List<Long> fromJson(String response) {
		List<Long> result;
	    if (response == null || response.isEmpty()) {
	        result = new ArrayList<>();
	    } else result =
	     Arrays.stream(response.split(";"))
	            .map(Long::valueOf)
	            .toList();
		return result;
	}
	@Override
	public List<Long> getStartedGamesWithGamer(String username) {
		String response = tcpClient.sendAndReceive(new Request("getStartedGamesWithGamer", username));
		return fromJson(response);
	}

	@Override
	public String loginGamer(String username) {
		return tcpClient.sendAndReceive(new Request("loginGamer", username));
	
	}


}
