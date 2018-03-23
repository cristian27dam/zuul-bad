import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */
public class Room 
{   
    // Descripcion de la sala actual del jugador
    private String description;
    // HashMap para las salidas posibles de la sala
    private HashMap<String, Room> salidasPosibles;
    // Item que contiene la sala
    private Item itemExistente;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description, Item itemExistente) 
    {
        this.description = description;
        salidasPosibles = new HashMap<>();
        this.itemExistente = itemExistente;
    }

    /**
     * Metodo para mapear cada salida posible de la sala de forma individual
     * y asi poder "inventar" direcciones desde la clase Game.
     * 
     * @param   direccion   La direccion de la salida de la sala.
     * @param   Room        La sala de destino.
     */
    public void setSalidaIndividual(String direccion, Room nuevaSalida) 
    {   
        salidasPosibles.put(direccion, nuevaSalida);
    }

    /**
     * Devuelve la descripcion de la sala actual.
     * 
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Metodo que devuelve la sala asociada a la direccion indicada por el jugador
     * 
     * @param   Un String que indica la direccion indicada por el jugador.
     * @return  Un objeto Room que representa una sala existente en dicha direccion o null en caso contrario.
     */
    public Room getExit(String direccion){
        Room salidaADevolver = salidasPosibles.get(direccion);

        return salidaADevolver;        
    }

    /**
     * Metodo que concatena el String de salidas posibles desde la sala actual.
     *
     * @return Un String informando de la salidas disponibles desde la posicion actual.
     */
    public String getExitString(){
        String salidasADevolver = "";

        // Se concatenan las salidas posibles de la sala actual
        for(String salidaPosible : salidasPosibles.keySet()){
            if (salidasPosibles.get(salidaPosible) != null){
                salidasADevolver += salidaPosible + " ";
            }
        }

        return salidasADevolver.trim();
    }

    /**
     * Return a long description of this room, of the form:
     *     You are in the 'name of room'
     *     Exits: north west southwest
     * @return A description of the room, including exits.
     */
    public String getLongDescription(){
        return getDescription() + "\nSalidas ---> " + getExitString();
    }
}
