import java.util.Stack;
import java.util.ArrayList;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    // Sala actual
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
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        visitedRooms = new Stack();
        playerItems = new ArrayList();
        pesoActual = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room entradaSalida, corredor1, salaAgua, salaFuego, corredor2, corredor3, salaElectrica, salaLLave, corredor4, corredor5;

        // Inicializacion de las salas de la mazmorra con su descripcion
        entradaSalida = new Room("Entrada/Salida: Estas confundido y mareado.. Ves una puerta cerrada detras de ti.. \nNotas algo de humedad en el ambiente..");
        corredor1 = new Room("Corredor 1: Un pasadizo donde se escucha ruido cerca..");
        salaAgua = new Room("Sala de Agua: Te encuentras en una sala llena de canyerias desde donde parece \nque se puede controlar el sistema hidraulico de este lugar..");
        salaFuego = new Room("Sala de Fuego: En esta sala ves llamas por todas partes y resulta sofocante estar mucho tiempo aqui..");
        corredor2 = new Room("Corredor 2: Un pasadizo con extranyos simbolos dibujados en la pared..\nEn el techo puedes ver una trampilla con un gran simbolo iluminado");
        corredor3 = new Room("Corredor 3: A tu alrededor hay objetos que parecen demasiado modernos para este sitio \n que tienen grabados parecidos a los que has visto..");
        salaElectrica = new Room("Sala electrica: Parece una antigua sala desde donde gestionaban la energia.. \nVes maquinas de todo tipo que desconoces..");
        salaLLave = new Room ("Sala misteriosa: Puede que aqui encuentres algo que te ayude a salir de este lugar.. \nVes un pilar con una hendidura conocida..");
        corredor4 = new Room("Corredor 4: Los dibujos de las paredes ahora parecen brillar...");
        corredor5 = new Room("Corredor 5: Parece que este lugar comunica con la sala de control de agua \nporque recuerdas ese sonido con claridad..,");

        // Inicializacion de los items de cada sala
        salaLLave.addItem(new Item("Parece que hay un cofre", 3000, "Cofre", false));
        corredor3.addItem(new Item("Hay una bateria tirada en una esquina", 1000, "Bateria", true));
        salaAgua.addItem(new Item("Hay una llave de paso que parece funcionar", 500, "LLave de paso", false));
        salaAgua.addItem(new Item("Hay un objeto brillante en una esquina", 500, "Amuleto", true));
        corredor5.addItem(new Item("Parece que hay una carta medio quemada sobre una mesa", 200, "Carta",true));
        salaElectrica.addItem(new Item("Una llave sobresale de una muesca de uno de los pilares de la sala", 300, "LLave del cofre", true));
        salaLLave.addItem(new Item("Otra llave con los simbolos que has visto cuelga de la pared", 300, "LLave desconocida",true));
        
        // Mapeo de cada salida de cada sala al momento de creacion
        entradaSalida.setSalidaIndividual("north", corredor1);
        entradaSalida.setSalidaIndividual("north-west", salaAgua);
        corredor1.setSalidaIndividual("south", entradaSalida);
        corredor1.setSalidaIndividual("west", salaAgua);
        corredor1.setSalidaIndividual("east", salaFuego);
        salaAgua.setSalidaIndividual("east", corredor1);
        salaAgua.setSalidaIndividual("south-east", entradaSalida);
        salaFuego.setSalidaIndividual("west", corredor1);
        salaFuego.setSalidaIndividual("north", corredor2);
        corredor2.setSalidaIndividual("north", corredor3);
        corredor2.setSalidaIndividual("south", salaFuego);
        corredor3.setSalidaIndividual("south", corredor2);
        corredor3.setSalidaIndividual("east", salaElectrica);
        corredor3.setSalidaIndividual("west", salaLLave);
        salaElectrica.setSalidaIndividual("west", corredor3);
        salaLLave.setSalidaIndividual("east", corredor3);
        salaLLave.setSalidaIndividual("west", corredor4);
        corredor4.setSalidaIndividual("east", salaLLave);
        corredor4.setSalidaIndividual("south", corredor5);
        corredor5.setSalidaIndividual("north", corredor4);
        corredor5.setSalidaIndividual("south", salaAgua);
        corredor5.setSalidaIndividual("south-east", corredor1);

        // Por mi planteamiento del mapa y sus "mecanicas" no se pueden mapear estas posibles direcciones
        // a pesar de que existan pero como no tenemos nada implementado las especifico igualmente
        salaAgua.setSalidaIndividual("north", corredor5);
        corredor1.setSalidaIndividual("north-west", corredor5);

        // Sala donde empieza el jugador
        currentRoom = entradaSalida;
    }

    /**
     *  Metodo que controla el inicio y fin del juego alternando entre recibir y comprobar si el comando
     *  introducido es valido y en ese caso invocar los metodos oportunos, ya sea goRoom() (moverse), 
     *  printHelp() (ayuda al usuario) o invertir la variable finished usando "quit".
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bienvenido a World of Signs!");
        System.out.println("World of Signs es un simple juego adaptado para nuestra clase de Java! ^^");
        System.out.println("Escribe 'help' para mas informacion.");
        System.out.println();
        printLocationInfo(); // Imprime localizacion y posibles salidas
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("look")){
            look();
        }
        else if (commandWord.equals("eat")){
            eat();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("items")) {
            showItems();
        }
        else if(commandWord.equals("take")){
            take(command.getSecondWord());
        }
        else if(commandWord.equals("drop")){
            drop(command.getSecondWord());
        }
        else{
            if ((commandWord.equals("back") && !visitedRooms.empty())){
                // Almacenamos la ultima sala apilada en el Stack de habitaciones
                // visitadas y la borramos del historial
                currentRoom = visitedRooms.pop();
            }
            else{
                System.out.println("¡No puedes volver atras, tienes que moverte primero!");
            }
            printLocationInfo();
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Te despiertas en una mazmorra. No sabes que ha pasado..");
        System.out.println("pero tienes que salir de ahí..");
        System.out.println();
        System.out.println("Los comandos disponibles son:");
        System.out.println(parser.showCommands()); // Imprime tantos comandos como existan en la clase CommandWords
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        // La direccion que solicita el usuario
        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            // Apilamos la sala actual en el Stack de salas visitadas antes de movernos
            // cuando la sala a la que moverse exista y asi poder ejecutar back a ella
            visitedRooms.push(currentRoom);
            currentRoom = nextRoom;
            printLocationInfo(); // Imprime localizacion y posibles salidas

        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Metodo privado que nos informa en que sala nos encontramos
     * e imprime las posibles salidas desde ese punto actual.
     */
    private void printLocationInfo(){
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Metodo privado que nos permite imprimir la descripcion y salidas posibles
     * de la sala actual en la que esta el jugador
     */
    private void look(){
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Metodo privado que nos permite simular que el personaje ha comido
     * y se ha aumentando un valor ficticio de "cantidad de hambre".
     */
    private void eat(){
        System.out.println("Acabas de comer y ya no tienes hambre");
    }
    
    /**
     * Metodo que nos permite mostrar por pantalla informacion relacionada con los items del inventario y el peso actual.
     */
    private void showItems(){
        if (!playerItems.isEmpty()){
            String listadoItems = "";
            int contador;
            contador = 1;
            for (Item item : playerItems){
                listadoItems += "\n" + contador + " [" + item.getItemName() + "] " + " " + item.getItemDescription();
                contador++;
            }
            System.out.println("Tu inventario contiene:\n" + listadoItems.trim());
        }
        else{
            System.out.println("¡No tienes ningun item en tu inventario actualmente!");
        }
        System.out.println("El peso actual del inventario es: " + getPesoActual() + "/1300");
    }

    /**
     * Metodo que permite recoger un item de la sala actual donde se encuentra el jugador.
     * 
     * @param   String  El nombre que aparece en el listado por pantalla de la informacion de la sala.
     */
    private void take(String itemIndicado){
        Item posibleItem = null;
        // Si el criterio introducido es un numero
        if (controlDatos(itemIndicado) == true ){
            // Si el array de items contiene algo
            if (!currentRoom.getItems().isEmpty()){
                // Si el indice solicitado del Array es valido
                if (Integer.parseInt(itemIndicado) -1  >= 0 && Integer.parseInt(itemIndicado) -1 < currentRoom.getItems().size()){
                    posibleItem = currentRoom.recogerItem(Integer.parseInt(itemIndicado) - 1);
                    // Evalua si se puede coger y si la suma de su peso supera el limite
                    if (posibleItem.getManipulable() == true && (posibleItem.getItemWeight() + pesoActual < LIMITE_CARGA)){
                        playerItems.add(posibleItem);
                        pesoActual += posibleItem.getItemWeight();
                        currentRoom.getItems().remove(posibleItem);
                        System.out.println("Has recogido el item: " + posibleItem.getItemName());
                        System.out.println("El peso actual del inventario es: " + getPesoActual() + "/1300");
                    }
                    // El item no es manipulable
                    else if(posibleItem.getManipulable() == false){
                        System.out.println("¡Este item no se puede manipular!");
                    }
                    else{
                        System.out.println("No puedes cargar con ese item, superas tu limite de carga");
                        System.out.println("El peso actual del inventario es: " + getPesoActual() + "/1300");
                    }
                }
                else{
                    System.out.println("¡No existe el item seleccionado en la sala actual!");
                }
            }
            // En caso de que no exista el item indicado
            else{
                System.out.println("¡No hay ningun item en la sala!");
            }
        }
        else{
            System.out.println("No ha introducido un patron de busqueda correcto");
        }
    }

    /**
     * Metodo que permite soltar items en la sala actual donde se encuentra el jugador.
     * 
     * @param String    Nombre del item que aparece en el listado por pantalla.
     */
    private void drop(String itemIndicado){
        if (controlDatos(itemIndicado) == true){
            if (!playerItems.isEmpty()){
                if((Integer.parseInt(itemIndicado) -1  >= 0 && Integer.parseInt(itemIndicado) -1 < playerItems.size())){
                    Item itemASoltar = playerItems.get(Integer.parseInt(itemIndicado) - 1);
                    currentRoom.addItem(new Item(itemASoltar.getItemDescription(), itemASoltar.getItemWeight(), itemASoltar.getItemName(), itemASoltar.getManipulable()));
                    System.out.println("Has dejado en la sala: " + itemASoltar.getItemName());
                    pesoActual -= itemASoltar.getItemWeight();
                    System.out.println("El peso actual del inventario es: " + getPesoActual() + "/1300");
                    playerItems.remove(itemASoltar);
                }
                else{
                    System.out.println("No existe el item seleccionado en tu inventario");
                }
            }
            else{
                System.out.println("No tienes ningun objeto en el invetario para soltar");
            }
        }
        else{
            System.out.println("No ha introducido un patron de busqueda correcto");
        }
    }

    /**
     * Metodo para evaluar si el patron usado para hacer take o drop es valido.
     * Detecta si es parseable el criterio introducido para usarlo en el ArrayList de items de la sala.
     * 
     * @param   El String a parsear para comprobar que corresponde a un indice valido
     * @return  True si se trata de un numero, false si no lo es
     */
    private boolean controlDatos(String posibleItem){
        boolean itemCorrecto;
        try {
            Integer.parseInt(posibleItem);
            itemCorrecto = true;
        } catch (NumberFormatException excepcion) {
            itemCorrecto = false;
        }

        return itemCorrecto;
    }
    
    /**
     * Getter del peso actual con los items del inventario
     * 
     * @return  Un entero que representa la suma de pesos de los items del inventario del jugador
     */
    private int getPesoActual(){
        return pesoActual;
    }
}
