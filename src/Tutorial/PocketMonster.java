package Tutorial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class PocketMonster implements Comparable<PocketMonster>{
    private static Random rand = new Random();
    private String name;
    private int hp;
    private int damage;
    private boolean isShiny;
    private Type type;

    public PocketMonster(String name, int hp, int damage, boolean isShiny, Type type) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.isShiny = isShiny;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isShiny() {
        return isShiny;
    }

    public void setShiny(boolean shiny) {
        isShiny = shiny;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Used for randomization purposes, and testing!
     * Returns a randomized pocketmonster
     * @return a random pocketmonster
     */
    public static PocketMonster createPocketMonster(){
        int HP = rand.nextInt(100) + 5;
        int DMG = rand.nextInt(20) + 5;
        String name = generateName();
        boolean isShiny = rand.nextBoolean();
        int typePick = rand.nextInt(Type.values().length);
        Type type = Type.values()[typePick];
        return new PocketMonster(name, HP, DMG, isShiny, type);
    }

    private static String generateName(){
        try{
            return getNameFromInternet();
        }catch(IOException ex){
            return nextStringOffline();
        }
    }

    private static String nextStringOffline() {
        if (rand == null) {
            rand = new Random(System.nanoTime());
        }
        char[] output = new char[rand.nextInt(15) + 3];
        for (int i = 0; i < output.length; i++) {
            output[i] = (char) rand.nextInt(490);
        }
        return new String(output);
    }

    private static String getNameFromInternet() throws IOException {
        return queryAPI("http://names.drycodes.com/1", true);
    }

    private static String queryAPI(String URLspec, boolean isSingular) throws IOException {
      /*  Runnable r = () -> {queryAPIsync(URLspec, isSingular);};
        Thread t1 = new Thread(r);
        t1.start();*/       //Async system is unnecessarily complex and not needed here
        return queryAPIsync(URLspec, isSingular);
    }

    private static String queryAPIsync(String URLspec, boolean isSingular) throws IOException {

            URL names = new URL(URLspec);
            // Get the input stream through URL Connection
            URLConnection con = names.openConnection();
            InputStream is = con.getInputStream();


            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;
            if ((line = br.readLine()) != null) {
                String name = line;
                if (isSingular) name = line.split("_")[0];
                name = name.replace("\"", "");
                name = name.replace("[", "");
                name = name.replace("]", "");
                name = name.replace("_", " ");
                return name;
            }
        throw new IOException("Oops! Query failed! Cant't help being a gemeni");
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(PocketMonster o) {
        int myScore = this.getDamage()+ this.getHp();
        int theirScore = o.getDamage() + o.getHp();
        return myScore - theirScore;
    }

    public String toString(){
        return " Score of this is: " + ((int)this.getDamage()+ (int)this.getHp()) + " "  + this.getName();
    }
}

enum Type{
    FIRE, WATER, FLYING, ELECTRIC
}