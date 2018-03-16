
/**
 * Clase que describe posibles objetos (Items) que pueden encontrarse en las salas del juego.
 *
 * @author Cristian Martinez Garcia
 * @version v1
 */
public class Item
{
    private String itemDescription;
    private int itemWeight;
    /**
     * Contructor de objetos Item
     */
    public Item(String itemDescription, int itemWeight)
    {
        this.itemDescription = itemDescription;
        this.itemWeight = itemWeight;
    }

    /**
     * Getter para la descripcion del Item
     */
    public String getItemDescription(){
        return itemDescription;
    }
    
    /**
     * Getter para el peso del Item
     */
    public int getItemWeight(){
        return itemWeight;
    }
    
    /**
     * Metodo para devolver la informacion del Item
     */
    public String getInfoItem(){
        return getItemDescription() + "Peso: " + getItemWeight();
    }
}
