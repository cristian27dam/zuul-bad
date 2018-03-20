
/**
 * Clase que describe posibles objetos (Items) que pueden encontrarse en las salas del juego.
 *
 * @author Cristian Martinez Garcia
 * @version v1
 */
public class Item
{   
    // Descripcion del item
    private String itemDescription;
    // Peso del item
    private int itemWeight;
    // Nombre del item
    private String itemName;
    // Propiedad que indica si se puede coger/manipular
    private boolean esManipulable;
    /**
     * Contructor de objetos Item
     * 
     * @param   itemDescription
     */
    public Item(String itemDescription, int itemWeight, String itemName, boolean esManipulable)
    {
        this.itemDescription = itemDescription;
        this.itemWeight = itemWeight;
        this.itemName = itemName;
        this.esManipulable = esManipulable;
    }

    /**
     * Getter para la descripcion del Item
     * 
     * @return  Un String con la descripcion del Item
     */
    public String getItemDescription(){
        return itemDescription;
    }
    
    /**
     * Getter para el peso del Item
     * 
     * @return Un entero que representa el peso del Item
     */
    public int getItemWeight(){
        return itemWeight;
    }
    
    /**
     * Getter para el nombre del Item
     * 
     * @return Un String que representa un nombre para el Item
     */
    public String getItemName(){
        return itemName;
    }
    
    /**
     * Metodo para devolver la informacion del Item
     * 
     * @return  Un string con la desripcion del item
     */
    public String getInfoItem(){
        return itemName + ": " + " Peso: " + itemWeight + " " + itemDescription;
    }
    
    /**
     * Getter de la propiedad esManipulable
     * 
     * @return  True si se puede coger, False si no se puede.
     */
    public boolean getManipulable(){
        return esManipulable;
    }
}
