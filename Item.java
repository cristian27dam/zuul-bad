
/**
 * Clase que describe posibles objetos (Items) que pueden encontrarse en las salas del juego.
 *
 * @author Cristian Martinez Garcia
 * @version v1
 */
public class Item
{   
    private String id;
    private String itemDescription;
    private int itemWeight;
    private boolean canBePickedUp;
    /**
     * Contructor de objetos Item
     * 
     * @param   itemDescription
     */
    public Item(String itemDescription, int itemWeight, String id, boolean canBePickedUp)
    {
        this.itemDescription = itemDescription;
        this.itemWeight = itemWeight;
        this.id = id;
        this.canBePickedUp = canBePickedUp;
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
     * Metodo para devolver la informacion del Item
     * 
     * @return  Un string con la desripcion del item
     */
    public String getInfoItem(){
        return "id: " + id + " " + "Descripcion: " + getItemDescription() + " Peso: " + getItemWeight();
    }
    
    public String getId(){
        return id;
    }
}
