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
    private int limiteCarga = 1300;
    // Flag para la funcionalidad especial (reducir peso)
    private boolean sobrecarga;

    /**
     * Constructor para las instancias de jugadores
     */
    public Player(Room startingRoom)
    {
        visitedRooms = new Stack<>();
        playerItems = new ArrayList<>();
        currentRoom = startingRoom;
        pesoActual = 0;
        sobrecarga = false;
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
                if (posibleItem.getItemWeight() + pesoActual <= limiteCarga){
                    playerItems.add(posibleItem); // Se agrega el item seleccionado al inventario
                    currentRoom.removeItem(posibleItem); // Se elimina del la sala actual
                    if (sobrecarga){
                        pesoActual += posibleItem.getItemWeight() / 2; // Nuevo peso con item especial
                    }
                    else{
                        pesoActual += posibleItem.getItemWeight(); // Nuevo peso sin item especial
                    }
                    System.out.println("Se ha agregado al inventario el item: " + posibleItem.getId()); // Info item
                    System.out.println("El peso del inventario es: " + pesoActual + "/" + limiteCarga + "\n");
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
        String infoAuxiliar = "¡SOBRECARGA SIN ACTIVAR!";
        if (sobrecarga){
            infoAuxiliar = "¡SOBRECARGA ACTIVIADA, REDUCCION DE PESO ITEMS DEL INVENTARIO Y SIGUIENTES!...";
        }
        if (!playerItems.isEmpty()){
            System.out.println(infoAuxiliar); // Mostrar si la sobrecarga ha sido activada
            for (Item itemActual : playerItems){
                System.out.println(itemActual.getInfoItem());
            }
        }
        else{
            System.out.println("No tienes objetos en tu inventario");
        }
        System.out.println("El peso del inventario es: " + pesoActual + "/" + limiteCarga);
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
                        if (sobrecarga){
                            pesoActual -= itemActual.getItemWeight() / 2; // Nuevo peso con item especial
                        }
                        else{
                            pesoActual -= itemActual.getItemWeight(); // Nuevo peso sin item especial
                        }
                        System.out.println("Has soltado el item: " + itemActual.getId()); // Info item
                        System.out.println("El peso de tu inventario actual es: " + pesoActual + "/" + limiteCarga); // Info inventario
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

    /**
     * Metodo que comprueba si se dan las condiciones para llamar y ejecutar la sobrecarga del inventario
     * implementando la funcionalidad solicitada.
     * 
     * @param   item    Un string que determina sobre que item se ha utilizado el comando recharge
     */
    public void changePlayerInventory(String posibleItem){

        // Que el item sobre el que se invoca recharge sea la bateria,
        // que se encuentre en la sala correcta y que en el inventario existan items actualmente
        if (posibleItem.equals("bateria") && getCurrentRoom().getDescription().contains("Sala electrica") && playerItems.size() > 0) {
            int indiceItems = 0;
            boolean encontrado = false;
            // Comprobamos si en el inventario esta el item con id "bateria"
            while (indiceItems < playerItems.size() && !encontrado){
                if (playerItems.get(indiceItems).getId().equals(posibleItem)){
                    //Mensaje que informa de la sobrecarga al usuario
                    System.out.println("Te sientes mas ligero, el peso de los items del inventario se reduce a la mitad \ny a partir de este momento el peso de cualquier item recogido sera de la mitad \n");

                    int pesoAcumulado = 0;

                    for (Item item : playerItems){
                        pesoAcumulado += item.getItemWeight();
                    }

                    // Se quita de la carga actual la mitad del peso de los items del inventario actuales
                    pesoActual -= pesoAcumulado / 2;
                    sobrecarga = true; // flag de la reduccion para los siguientes items
                    encontrado = true; // termina el while
                }
                indiceItems++;
            }
        }
        else{
            System.out.println("No se puede usar el comando en este momento");
        }

        //Se vuelve a mostrar la informacion de la sala
        look();
    }
}
