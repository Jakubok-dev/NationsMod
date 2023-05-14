package me.jakubok.nationsmod.collection;

import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class Name {
    public final String firstName;
    public final String lastName;

    public Name(boolean isAFemale) {
        this(randomSurname(), isAFemale);
    }
    public Name(String lastName, boolean isAFemale) {
        this.firstName = randomName(isAFemale);
        this.lastName = lastName;
    }
    public Name(boolean isAFemale, UUID uuid) {
        this(surnameFromUUID(uuid), isAFemale, uuid);
    }
    public Name(String lastName, boolean isAFemale, UUID uuid) {
        this.firstName = nameFromUUID(isAFemale, uuid);
        this.lastName = lastName;
    }
    public Name(NbtCompound nbt) {
        this.firstName = nbt.getString("firstName");
        this.lastName = nbt.getString("lastName");
    }

    public Text getText() {
        return Text.of(firstName + " " + lastName);
    }

    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putString("firstName", this.firstName);
        tag.putString("lastName", this.lastName);
        return tag;
    }

    public static String randomName(boolean female) {
        Random rng = new Random();
        return female ? femaleNames[rng.nextInt(femaleNames.length)] : maleNames[rng.nextInt(maleNames.length)];
    }

    public static String randomSurname() {
        Random rng = new Random();
        return surnames[rng.nextInt(surnames.length)];
    }

    public static String nameFromUUID(boolean female, UUID uuid) {
        return female ? femaleNames[Math.floorMod(uuid.hashCode(), femaleNames.length)] : maleNames[Math.floorMod(uuid.hashCode(), maleNames.length)];
    }

    public static String surnameFromUUID(UUID uuid) {
        return surnames[Math.floorMod(uuid.hashCode(), surnames.length)];
    }

    public static String[] maleNames = {
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
        "Roger",
        "Terry",
        "Kai",
        "Sunny",
        "Zuri"
    };

    public static String[] femaleNames = {
        "Olivia",
        "Emma",
        "Charlotte",
        "Amelia",
        "Ava",
        "Sophia",
        "Isabella",
        "Mia",
        "Evelyn",
        "Harper",
        "Mary",
        "Patricia",
        "Jennifer",
        "Alex",
        "Efe",
        "Makena",
        "Noor",
        "Linda",
        "Elizabeth",
        "Barbara",
        "Susan",
        "Jessica",
        "Sarah",
        "Karen",
        "Lisa",
        "Nancy",
        "Betty",
        "Margaret",
        "Sandra",
        "Ashley",
        "Kimberly",
        "Emily",
        "Donna",
        "Michelle",
        "Carol",
        "Amanda",
        "Dorothy",
        "Melissa",
        "Deborah",
        "Stephanie",
        "Rebecca",
        "Amanda",
        "Sharon",
        "Laura",
        "Cynthia",
        "Kathleen",
        "Amy",
        "Angela",
        "Shirley",
        "Anna",
        "Brenda",
        "Pamela",
        "Emma",
        "Nicole",
        "Helen",
        "Samantha",
        "Katherine",
        "Christine",
        "Debra",
        "Rachel",
        "Carolyn",
        "Janet",
        "Catherine",
        "Maria",
        "Heather",
        "Diane",
        "Ruth",
        "Julie",
        "Olivia",
        "Joyce",
        "Kelly",
        "Victoria",
        "Lauren",
        "Christina",
        "Joan",
        "Evelyn",
        "Ari"
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

    public static final TrackedDataHandler<Name> NAME_HANDLER = new TrackedDataHandler<Name>() {

        @Override
        public void write(PacketByteBuf var1, Name var2) {
            var1.writeNbt(var2.writeToNbtAndReturn(new NbtCompound()));
        }

        @Override
        public Name read(PacketByteBuf var1) {
            return new Name(var1.readNbt());
        }

        @Override
        public Name copy(Name var1) {
            return var1;
        }
        
    }; 
}
