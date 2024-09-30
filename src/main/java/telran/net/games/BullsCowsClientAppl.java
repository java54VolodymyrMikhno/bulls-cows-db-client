package telran.net.games;

import telran.net.TcpClient;
import telran.net.games.service.BullsCowsService;
import telran.net.games.service.BullsCowsTcpProxy;
import telran.view.Item;
import telran.view.Menu;
import telran.view.SystemInputOutput;

import java.util.List;



public class BullsCowsClientAppl {
    private static final int PORT = 5000;
    private static final int N_DIGITS = 4;

	public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost",PORT);
        BullsCowsService service = new BullsCowsTcpProxy(tcpClient);
        List<Item> menuItems = BullsCowsApplicationItems.getItems(service,N_DIGITS);
        menuItems.add(Item.of("Exit & close connection", io -> tcpClient.close(),true));
        Menu menu = new Menu("Bulls and Cows Main Menu",menuItems.toArray(Item[]::new));
        menu.perform(new SystemInputOutput());
    }


}
