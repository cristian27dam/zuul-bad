import java.util.HashMap;
import java.util.ArrayList;

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
    // ArrayList para los items de cada Sala
    private ArrayList<Item> itemsExistentes;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description){
        {
            this.description = description;
            salidasPosibles = new HashMap<>();
            itemsExistentes = new ArrayList<>();
        }
    }

    /**
     * Metodo para mapear cada salida posible de la sala de forma individual
     * y asi poder "inventar" direcciones desde la clase Game.
     * 
     * @param   direccion   La direccion de la salida de la sala.
     * @param   nuevaSalida La sala de destino.
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
     * @param   direccion   Un String que indica la direccion indicada por el jugador.
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
        String descripcion = getDescription() + "\nSalidas ---> " + getExitString();
        String infoItems = "";
        int contador;
        contador = 1;
        if (!itemsExistentes.isEmpty()){
            for (Item itemSalaActual : itemsExistentes){
                infoItems += "\n" + contador + ". " + itemSalaActual.getInfoItem();
                contador++;
            }
            descripcion = getDescription() + "\nLos items disponibles son:" + infoItems + "\nSalidas ---> " + getExitString();
        }
        return descripcion;
    }

    /**
     * Metodo que nos permite agregar tantos items como queramos a la sala actual
     * 
     * @param   nuevoItem   Un objeto Item que estara incluido en la sala.
     */
    public void addItem(Item nuevoItem){
        itemsExistentes.add(nuevoItem);
    }

    /**
     * Metodo para devolver la coleccion de items de la sala actual y poder manipularla
     * desde Game.
     * 
     * @return Un ArrayList con los items que se encuentran en la sala actual del jugador.
     */
    public ArrayList<Item> getItems(){
        return itemsExistentes;
    }
    
    /**
     * Metodo que permite recoger un item de la sala actual si dicho item se puede obtener.
     * 
     * @param   indiceItem  Un indice que representa la posicion de un item en la lista de la sala actual.
     * @return Devuelve un objeto Item.
     */
    public Item recogerItem(int indiceItem){
        return itemsExistentes.get(indiceItem);
    }
}
