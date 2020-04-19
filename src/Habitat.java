public class Habitat {

    public static long NOrdinary, NAlbino; // milliseconds
    public static double KAlbino, POrdinary; // [0, 1]

    private static AbstactFactory factory = new RabbitFactory();

    private static GUI gui;

    Habitat(int Width, int Height) {
        gui = new GUI(Width, Height);
    }

    public static void Update(long time) {
        if (time % NOrdinary == 0 && Math.random() < POrdinary) {
            Singleton.getVector().add(factory.creatOrdinary(Math.random(), Math.random(), time));
            Singleton.getTreeSetID().add(Singleton.getVector().lastElement().ID);
            Singleton.getHashMap().put(Singleton.getVector().lastElement().ID, Singleton.getVector().lastElement().BirthTime);
        }
        if (time % NAlbino == 0 && (double)Albino.getAlbinoQuantity() / Rabbit.getAllQuantity() < KAlbino) {
            Singleton.getVector().add(factory.creatAlbino(Math.random(), Math.random(), time));
            Singleton.getTreeSetID().add(Singleton.getVector().lastElement().ID);
            Singleton.getHashMap().put(Singleton.getVector().lastElement().ID, Singleton.getVector().lastElement().BirthTime);
        }
        for (int i = 0; i < Singleton.getVector().size(); i++) {
            if (Singleton.getVector().get(i) instanceof Ordinary) {
                if (time == Singleton.getVector().get(i).BirthTime + Ordinary.TimeOfLife) {
                    Rabbit.AllQuantity--;
                    Ordinary.OrdinaryQuantity--;
                    Singleton.getHashMap().remove(Singleton.getVector().get(i).ID);
                    Singleton.getTreeSetID().remove(Singleton.getVector().get(i).ID);
                    Singleton.getVector().remove(i);
                }
            } else {
                if (time == Singleton.getVector().get(i).BirthTime + Albino.TimeOfLife) {
                    Rabbit.AllQuantity--;
                    Albino.AlbinoQuantity--;
                    Singleton.getHashMap().remove(Singleton.getVector().get(i).ID);
                    Singleton.getTreeSetID().remove(Singleton.getVector().get(i).ID);
                    Singleton.getVector().remove(i);
                }
            }
        }
        gui.Render();
    }


}
