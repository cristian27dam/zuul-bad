import java.util.Stack;
import java.util.ArrayList;
/**
 * Write a description of class player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    // Habitacion actual del player
    private Room currentRoom;
    // Stack de salas visitadas
    private Stack<Room> visitedRooms;
    // Inventario de items del jugador
    private ArrayList<Item> playerItems;
    // Peso actual del inventario
    private int pesoActual;
    // Constante que define el limite de peso
    private final int LIMITE_CARGA = 1300;

    /**
     * Constructor para las instancias de jugadores
     */
    public Player(Room startingRoom)
    {
        visitedRooms = new Stack<>();
        playerItems = new ArrayList<>();
        currentRoom = startingRoom;
    }

    /**
     * Setter de la habitacion actual del jugador;
     */
    public void setPlayerRoom(Room nextRoom)
    {
        currentRoom = nextRoom;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Metodo para evaluar si el jugador puede volver hacia atras
     */
    public void backToRoom(){
        if (!visitedRooms.isEmpty()){
            currentRoom = visitedRooms.pop();
            look();
        }
        else{
            System.out.println("¡No puedes volver atras, tienes que moverte primero!"); 
        }
    }

    /** 
     * Metodo para que el jugador se mueva por las salas
     * 
     * @param   comandoDeInterfaz La palabraque mander el parsers (la direccion)
     */
    public void goRoom(Command comandoDeInterfaz){
        if(!comandoDeInterfaz.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        String direction = comandoDeInterfaz.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            // Apilamos la sala actual en el Stack de salas visitadas antes de movernos
            // cuando la sala a la que moverse exista y asi poder ejecutar back a ella
            visitedRooms.push(currentRoom);
            currentRoom = nextRoom;
            look();
        }
    }

    /**
     * Metodo que nos permite imprimir la descripcion y salidas posibles
     * de la sala actual en la que esta el jugador
     */
    public void look(){
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Metodo que nos permite simular que el personaje ha comido
     * y se ha aumentando un valor ficticio de "cantidad de hambre".
     */
    public void eat(){
        System.out.println("Acabas de comer y ya no tienes hambre");
    }

    public void pickUpItem(String id){
        Item posibleItem = currentRoom.itemToPlayer(id);

        if (posibleItem != null){
            if (posibleItem.getItemWeight() + pesoActual < LIMITE_CARGA){
                playerItems.add(posibleItem);
                pesoActual += posibleItem.getItemWeight();
                currentRoom.removeItem(posibleItem);
                System.out.println("Se ha agregado el item: " + posibleItem.getInfoItem());
                System.out.println("El peso de tu inventario actual es: " + pesoActual + "/" + LIMITE_CARGA);
                look();
            }
            else{
                System.out.println("No puedes cargar con el item, superarias el limite de carga");
            }
        }
        else{
            System.out.println("No existe el Item indicado");
        }
    }

    public void showItems(){
        if (!playerItems.isEmpty()){
            String aDevolver = "";
            for (Item itemActual : playerItems){
                aDevolver += "\n" + itemActual.getInfoItem();
            }
            System.out.println(aDevolver);
        }
        else{
            System.out.println("No tienes objetos en tu inventario");
        }
    }
}
