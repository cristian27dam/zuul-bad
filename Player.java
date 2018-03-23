import java.util.Stack;
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

    /**
     * Constructor para las instancias de jugadores
     */
    public Player(Room startingRoom)
    {
        visitedRooms = new Stack<>();
        currentRoom = startingRoom;
    }

    /**
     * Setter de la habitacion actual del jugador;
     */
    public void setPlayerRoom(Room nextRoom)
    {
        currentRoom = nextRoom;
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
}
