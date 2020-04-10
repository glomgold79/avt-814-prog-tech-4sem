public class Habitat {

    public static long NOrdinary, NAlbino; // milliseconds
    public static double KAlbino, POrdinary; // [0, 1]

    private static AbstactFactory factory = new RabbitFactory();

    private static GUI gui;

    Habitat(int Width, int Height) {
        gui = new GUI(Width, Height);
    }

    public static void Update(long time) {
        if (time % NOrdinary == 0 && Math.random() < POrdinary) Singleton.getLink().add(factory.creatOrdinary(Math.random(), Math.random()));
        if (time % NAlbino == 0 && (double)Albino.getAlbinoQuantity() / Rabbit.getAllQuantity() < KAlbino) Singleton.getLink().add(factory.creatAlbino(Math.random(), Math.random()));
        gui.Render();
    }


}
