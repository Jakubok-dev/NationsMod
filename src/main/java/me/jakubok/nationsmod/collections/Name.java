package me.jakubok.nationsmod.collections;

import java.util.Random;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class Name implements ComponentV3 {
    public String firstName;
    public String lastName;

    public Name() {
        this(randomSurname());
    }
    public Name(String lastName) {
        this.firstName = randomName();
        this.lastName = lastName;
    }
    public Name(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public Text getText() {
        return Text.of(firstName + " " + lastName);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.firstName = tag.getString("firstName");
        this.lastName = tag.getString("lastName");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putString("firstName", this.firstName);
        tag.putString("lastName", this.lastName);
        return tag;
    }

    public static String randomName() {
        Random rng = new Random();
        return names[rng.nextInt(names.length)];
    }

    public static String randomSurname() {
        Random rng = new Random();
        return surnames[rng.nextInt(surnames.length)];
    }

    public static String[] names = {
        "Mark",
        "Steve",
        "Thomas",
        "Joe",
        "Donald",
        "Jacob",
        "Arthur",
        "William",
        "Noah",
        "James",
        "Henry",
        "Liam",
        "Oliver",
        "Benjamin",
        "Robert",
        "John",
        "Michael",
        "David",
        "Richard",
        "Joseph",
        "Charles",
        "Christopher",
        "Daniel",
        "Matthew",
        "Anthony",
        "Paul",
        "Andrew",
        "Joshua",
        "Kenneth",
        "Kevin",
        "Brian",
        "George",
        "Timothy",
        "Ronald",
        "Edward",
        "Jason",
        "Jeffrey",
        "Ryan",
        "Gary",
        "Nicholas",
        "Eric",
        "Jonathan",
        "Stephen",
        "Larry",
        "Justin",
        "Scott",
        "Brandon",
        "Samuel",
        "Gregory",
        "Alexander",
        "Frank",
        "Patrick",
        "Raymond",
        "Jack",
        "Dennis",
        "Jerry",
        "Tyler",
        "Aaron",
        "Jose",
        "Adam",
        "Nathan",
        "Harry",
        "Douglas",
        "Zachary",
        "Peter",
        "Kyle",
        "Ethan",
        "Walter",
        "Jeremy",
        "Christian",
        "Keith",
        "Roger"
    };

    public static String[] surnames = {
        "Kowalski",
        "Smith",
        "Brown",
        "Sklodowski",
        "Campbell",
        "Stewart",
        "Scott",
        "Marshall",
        "Stevenson",
        "Wood",
        "Hoxha",
        "Prifti",
        "Grigoryan",
        "Harutyunyan",
        "Gruber",
        "Huber",
        "Mammadov",
        "Aliyev",
        "Ivanow",
        "Kazlow",
        "Peeters",
        "Janssens",
        "Hodzic",
        "Cengic",
        "Kovacevic",
        "Subotic",
        "Dimitrov",
        "Nikolov",
        "Horvat",
        "Kovacevic",
        "Novak",
        "Svoboda",
        "Nielsen",
        "Jensen",
        "Joensen",
        "Hansen",
        "Tamm",
        "Saar",
        "Korhonen",
        "Virtanen",
        "Martin",
        "Bernard",
        "Beridze",
        "Mamedovi",
        "Muller",
        "Schmidt",
        "Samaras",
        "Papoutis",
        "Nagy",
        "Kovacs",
        "Blondal",
        "Thorarensen",
        "Murphy",
        "Walsh",
        "Rossi",
        "Ferrari",
        "Krasniqi",
        "Gashi",
        "Berzins",
        "Kalnins",
        "Kazlaukas",
        "Jankauskas",
        "Borg",
        "Camilleri",
        "Rusu",
        "Ceban",
        "Popovic",
        "Markovic",
        "De Jong",
        "Jansen",
        "Andov",
        "Angelsov",
        "Hansen",
        "Johansen",
        "Wojcik",
        "Lewandowski",
        "Silva",
        "Santos",
        "Popa",
        "Popescu",
        "Smirnov",
        "Kuznetsov",
        "Ilic",
        "Milosevic",
        "Varga",
        "Szabo",
        "Krajnc",
        "Kos",
        "Garcia",
        "Fernandez",
        "Gonzalez",
        "Rodriguez",
        "Andersson",
        "Karlsson",
        "Meier",
        "Weber",
        "Yilmaz",
        "Kaya",
        "Melnyk",
        "Shevchenko"
    };
}
