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
        pesoActual = 0;
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
    public void goRoom(String posibleDireccion){
        if (posibleDireccion != null){
            Room nextRoom = currentRoom.getExit(posibleDireccion);
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
        else{
            System.out.println("Go where?..");
        }
    }

    /**
     * Metodo que nos permite imprimir la descripcion y salidas e items posibles
     * de la sala actual en la que esta el jugador. 
     */
    public void look(){
        currentRoom.getLongDescription();
    }

    /**
     * Metodo que nos permite simular que el personaje ha comido
     * y se ha aumentando un valor ficticio de "cantidad de hambre".
     */
    public void eat(){
        System.out.println("Acabas de comer y ya no tienes hambre");
    }

    /**
     * Metodo que permite al personaje coger objetos Item de la sala actual.
     * 
     * @param   id  Un String que represente la palabra clave de un Item de la sala.
     */
    public void pickUpItem(String id){
        // Room trata de devoler el objeto solicitado
        Item posibleItem = currentRoom.itemToPlayer(id);

        // Comprobacion de errores
        if (posibleItem != null){
            if(posibleItem.getCanBePickedUp()){
                if (posibleItem.getItemWeight() + pesoActual <= LIMITE_CARGA){
                    playerItems.add(posibleItem); // Se agrega el item seleccionado al inventario
                    currentRoom.removeItem(posibleItem); // Se elimina del la sala actual
                    pesoActual += posibleItem.getItemWeight(); // Nuevo peso
                    System.out.println("Se ha agregado al inventario el item: " + posibleItem.getId()); // Info item
                    System.out.println("El peso del inventario es: " + pesoActual + "/" + LIMITE_CARGA + "\n");
                    look();
                }
                else{
                    System.out.println("No puedes cargar con el item, superarias el limite de carga..");
                }
            }
            else{
                System.out.println("Parece que el objeto no se puede recoger..");
            }
        }
    }

    /**
     * Imprime por pantalla informacion relacionada con el estado actual del inventario.
     */
    public void showItems(){
        if (!playerItems.isEmpty()){
            for (Item itemActual : playerItems){
                System.out.println(itemActual.getInfoItem());
            }
        }
        else{
            System.out.println("No tienes objetos en tu inventario");
        }
        System.out.println("El peso del inventario es: " + pesoActual + "/" + LIMITE_CARGA);
    }

    /**
     * Metodo que permite al jugador soltar un objeto Item de su inventario en la sala actual.
     * 
     * @param   id  Un String que represente la palabra clave de un Item del inventario del jugador.
     */
    public void dropItem(String id){
        if (id != null){
            // flag stop while
            boolean encontrado;
            encontrado = false;

            if (!playerItems.isEmpty()){
                for (int i = 0; i < playerItems.size() && !encontrado; i++){
                    Item itemActual = playerItems.get(i);
                    if (itemActual.getId().equals(id)){
                        encontrado = true; // Flag para parar el bucle
                        playerItems.remove(itemActual); // Se elimina del inventario
                        currentRoom.addItem(itemActual); // Se agrega a la sala actual
                        pesoActual -= itemActual.getItemWeight(); // Nuevo peso del inventario
                        System.out.println("Has soltado el item: " + itemActual.getId()); // Info item
                        System.out.println("El peso de tu inventario actual es: " + pesoActual + "/" + LIMITE_CARGA); // Info inventario
                        encontrado = true; // Flag stop
                    }
                }
                if (!encontrado){
                    System.out.println("No has introducido un id valido");
                }
            }
            else{
                System.out.println("No tienes nada en el inventario");
            }
        }
        else{
            System.out.println("Drop what?..");
        }
    }
}
