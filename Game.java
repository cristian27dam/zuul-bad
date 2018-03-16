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
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room entradaSalida, corredor1, salaAgua, salaFuego, corredor2, corredor3, salaElectrica, salaLLave, corredor4, corredor5;

        // Inicializacion de las salas de la mazmorra con su descripcion
        entradaSalida = new Room("Entrada/Salida: Estas confundido y mareado.. Ves una puerta cerrada detras de ti.. \nNotas algo de humedad en el ambiente..", null);
        corredor1 = new Room("Corredor 1: Un pasadizo donde se escucha ruido cerca..", null);
        salaAgua = new Room("Sala de Agua: Te encuentras en una sala llena de canyerias desde donde parece \nque se puede controlar el sistema hidraulico de este lugar..",
                    new Item("Hay una llave de paso que parece funcionar", 500));
        salaFuego = new Room("Sala de Fuego: En esta sala ves llamas por todas partes y resulta sofocante estar mucho tiempo aqui..", null);
        corredor2 = new Room("Corredor 2: Un pasadizo con extranyos simbolos dibujados en la pared..\nEn el techo puedes ver una trampilla con un gran simbolo iluminado", null);
        corredor3 = new Room("Corredor 3: A tu alrededor hay objetos que parecen demasiado modernos para este sitio \n que tienen grabados parecidos a los que has visto..", 
                    new Item("Hay una bateria tirada en una esquina", 1000));
        salaElectrica = new Room("Sala electrica: Parece una antigua sala desde donde gestionaban la energia.. \nVes maquinas de todo tipo que desconoces..", null);
        salaLLave = new Room ("Sala misteriosa: Puede que aqui encuentres algo que te ayude a salir de este lugar.. \nVes un pilar con una hendidura conocida..", 
                    new Item("Parece que hay un cofre", 3000));
        corredor4 = new Room("Corredor 4: Los dibujos de las paredes ahora parecen brillar...", null);
        corredor5 = new Room("Corredor 5: Parece que este lugar comunica con la sala de control de agua \nporque recuerdas ese sonido con claridad..,", null);

        // Mapeo de cada sala con sus posibles salidas al momento de creacion
        entradaSalida.setSalidaIndividual("north", corredor1);
        entradaSalida.setSalidaIndividual("north-west", salaAgua);
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
}
