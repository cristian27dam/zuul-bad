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
                salidasADevolver += "[" + salidaPosible + "]" + " ";
            }
        }

        return salidasADevolver.trim();
    }

    /**
     * Imprime por pantalla la informacion relacionada con la sala actual.
     *     ==================
     *     Nombre/Descripcion
     *     Salidas visibles
     *     Posible listado de items
     *     ==================
     */
    public void getLongDescription(){
        System.out.println(getDescription() + "\n");

        System.out.println("SALIDAS ----> " + getExitString());
        // Si existe algun item en la sala
        if (!itemsExistentes.isEmpty()){
            System.out.println("LISTA ITEMS:");
            for (Item itemSalaActual : itemsExistentes){
                System.out.println(itemSalaActual.getInfoItem() + "\n");
            }
        }
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
     * Metodo para devolver el posible item de la sala actual seleccionado por el jugador
     * 
     * @param   id  El id que identifica al objeto Item
     */
    public Item itemToPlayer(String id){
        Item posibleItem = null;
        boolean itemEncontrado;
        itemEncontrado = false;
        if (id != null){
            if (!itemsExistentes.isEmpty()){
                for (int i = 0; i < itemsExistentes.size() && !itemEncontrado; i++){
                    Item itemActual = itemsExistentes.get(i);
                    if (itemActual.getId().equals(id)){
                        posibleItem = itemActual;
                        itemEncontrado = true;
                    }
                }
                if (!itemEncontrado){
                    System.out.println("No has introducido un id valido");
                }
            }
            else{
                System.out.println("No hay items aqui.");
            }
        }
        else{
            System.out.println("Take what?..");
        }

        return posibleItem;
    }

    /**
     * Elimina un item de la sala actual
     * 
     * @param   itemToRemove    El objeto tipo Item a eliminar de la sala.
     */
    public void removeItem(Item itemToRemove){
        itemsExistentes.remove(itemToRemove);
    }
}