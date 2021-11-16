package src;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.midi.MidiParserListener;
import org.jfugue.theory.*;
import org.staccato.StaccatoParser;

import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MusicTheorySolver {

    public static void main(String[] args) {
        new MusicTheorySolver();
    }

    final int beatsPerMeasure = (int)(Math.random()*3)+2;
    int phraseIncrement;
    String musicString;
    String sopranoString;
    String altoString;
    String tenorString;
    String bassString;
    String[] soprano;
    String[] alto;
    String[] tenor;
    String[] bass;
    ChordProgression chordProgression;
    Key k;
    Object[] chordLengths;
    int[] phraseLengths;
    final int[] sopranoInstruments = {0, 40, 6, 19, 73, 64, 73, 56, 68, 40, 74};
    final int[] altoInstruments = {0, 40, 6, 19, 0, 65, 69, 60, 0, 0, 0};
    final int[] tenorInstruments = {0, 41, 6, 19, 0, 66, 71, 57, 0, 0, 0};
    final int[] bassInstruments = {0, 42, 6, 19, 0, 67, 70, 58, 0, 0, 0};

    public MusicTheorySolver() {
        phraseIncrement = 0;
        Note root = new Note("C");
        k = new Key(root, Scale.MAJOR);
        //chordProgression = new ChordProgression("");
        //chordProgression.setKey(k);
        phraseLengths = new int[(int)(Math.random()*2) + 2]; // Length in measures
        sopranoString = "";
        altoString = "";
        tenorString = "";
        bassString = "";
        for (; phraseIncrement < phraseLengths.length; phraseIncrement++) {
            do {
                phraseLengths[phraseIncrement] = (int) (Math.random() * 6) + 3;
                generateChordLengths(phraseLengths[phraseIncrement]);
                soprano = new String[chordLengths.length];
                alto = new String[chordLengths.length];
                tenor = new String[chordLengths.length];
                bass = new String[chordLengths.length];
                String temp = generateProgression(chordLengths.length).substring(1);
                //System.out.println(temp);
                chordProgression = new ChordProgression(temp);
                //System.out.println(Arrays.toString(chordProgression.getChords()));
                generateVoices();
            } while (phraseIncrement + 1 == phraseLengths.length && !soprano[soprano.length-1].substring(0, 1).equals("C"));

            generateVoiceStrings();
        }
        //System.out.println(sopranoString);
        //System.out.println(altoString);
        //System.out.println(tenorString);
        //System.out.println(bassString);
        generateMusicString();
        //System.out.println(musicString);
        showMusic();
    }

    public String generateProgression(int length)
    {
        return generateProgression(length, null);
    }

    private String generateProgression(int length, String next)
    {
        if (next == null)
        {
           if (phraseIncrement + 1 == phraseLengths.length)
           {
               return generateProgression(length - 2, "V") + "-V-I";
           }
           else
           {
               int rand = (int)(Math.random() * 3);
               if (rand == 0)
               {
                   return generateProgression(length-1, "V") + "-V";
               }
               else if (rand == 1)
               {
                   return generateProgression(length-2, "V") + "-V-vi";
               }
               else
               {
                   return generateProgression(length-2, "IV") + "-IV-I";
               }
           }
        }
        if (length != 0)
        {
            switch (next) {
                case "I": {
                    int rand = (int) (Math.random() * 3);
                    if (rand == 0) {
                        return generateProgression(length - 1, "IV") + "-IV";
                    } else if (rand == 1) {
                        return generateProgression(length - 1, "viio") + "-viio";
                    } else {
                        return generateProgression(length - 1, "V") + "-V";
                    }
                }
                case "ii": {
                    int rand = (int) (Math.random() * 3);
                    if (rand == 0) {
                        return generateProgression(length - 1, "IV") + "-IV";
                    } else if (rand == 1) {
                        return generateProgression(length - 1, "I") + "-I";
                    } else {
                        return generateProgression(length - 1, "vi") + "-vi";
                    }
                }
                case "iii":
                    return generateProgression(length - 1, "I") + "-I";
                case "IV": {
                    int rand = (int) (Math.random() * 5);
                    if (rand == 0 || rand == 1) {
                        return generateProgression(length - 1, "vi") + "-vi";
                    } else if (rand == 2 || rand == 3) {
                        return generateProgression(length - 1, "I") + "-I";
                    } else {
                        return generateProgression(length - 1, "iii") + "-iii";
                    }
                }
                case "V":
                case "viio": {
                    int rand = (int) (Math.random() * 3);
                    if (rand == 0) {
                        return generateProgression(length - 1, "ii") + "-ii";
                    } else if (rand == 1) {
                        return generateProgression(length - 1, "IV") + "-IV";
                    } else {
                        return generateProgression(length - 1, "I") + "-I";
                    }
                }
                case "vi": {
                    int rand = (int) (Math.random() * 5);
                    if (rand == 0 || rand == 1) {
                        return generateProgression(length - 1, "V") + "-V";
                    } else if (rand == 2 || rand == 3) {
                        return generateProgression(length - 1, "I") + "-I";
                    } else {
                        return generateProgression(length - 1, "iii") + "-iii";
                    }
                }
                default:
                    throw new RuntimeException();
            }
        }
        else return "";
    }

    public void generateVoices() {
        Chord[] chords = chordProgression.getChords();
        List<Integer> temp = new ArrayList<>();
        temp.add(0); temp.add(1); temp.add(2);
        Collections.shuffle(temp);
        bass[0] = Note.getToneStringWithoutOctave(chords[0].getRoot().getValue()) + "3";
        tenor[0] = Note.getToneStringWithoutOctave(chords[0].getNotes()[temp.get(0)].getValue());
        alto[0] = Note.getToneStringWithoutOctave(chords[0].getNotes()[temp.get(1)].getValue()) + "5";
        soprano[0] = Note.getToneStringWithoutOctave(chords[0].getNotes()[temp.get(2)].getValue());
        if ((new SimpleNote(soprano[0] + "6", k).note + new SimpleNote(soprano[0] + "6", k).octave * 8)  - (new SimpleNote(alto[0], k).note + new SimpleNote(alto[0], k).octave * 8) >= 8)
            soprano[0] += "5";
        else
            soprano[0] += "6";

        if ((new SimpleNote(tenor[0] + "5", k).note + new SimpleNote(tenor[0] + "5", k).octave * 8)  - (new SimpleNote(alto[0], k).note + new SimpleNote(alto[0], k).octave * 8) <= 0)
            tenor[0] += "5";
        else
            tenor[0] += "4";

        for (int x = 1; x < chords.length; x++) {
            int jump = new SimpleNote(chords[x].getRoot().toString(), k).note - new SimpleNote(chords[x-1].getRoot().toString(), k).note;
            //int jump = Integer.parseInt(chords[x].getIntervals().getNthInterval(0)) - Integer.parseInt(chords[x - 1].getIntervals().getNthInterval(0));
            jump += Math.signum(jump);
            bass[x] = Note.getToneStringWithoutOctave(chords[x].getRoot().getValue()) + "3";

            Note[] chordNotes = chords[x].getNotes();
            String[] chordNotesString = new String[chordNotes.length];
            for (int y = 0; y < chordNotes.length; y++) {
                chordNotesString[y] = Note.getToneStringWithoutOctave(chordNotes[y].getValue()).toLowerCase();
            }

            if (jump == -7 || jump == 2) {
                tenor[x] = tenor[x - 1];
                while (!Arrays.asList(chordNotesString).contains(new SimpleNote(tenor[x], k).getNote().toLowerCase())) {
                    tenor[x] = new SimpleNote(tenor[x], k).getLowerNote();
                }

                alto[x] = alto[x - 1];
                while (!Arrays.asList(chordNotesString).contains(new SimpleNote(alto[x], k).getNote().toLowerCase())) {
                    alto[x] = new SimpleNote(alto[x], k).getLowerNote();
                }

                soprano[x] = soprano[x - 1];
                //if (soprano[x-1].substring(0, 1).equals("B"))
                //{
                //    soprano[x] = "C" + (Integer.parseInt(soprano[x-1].substring(1)) + 1);
                //}
                //else
                //{
                    while (!Arrays.asList(chordNotesString).contains(new SimpleNote(soprano[x], k).getNote().toLowerCase())) {
                        soprano[x] = new SimpleNote(soprano[x], k).getLowerNote();
                    }
                //}
            }
            if (jump == 7 || jump == -2) {
                tenor[x] = tenor[x - 1];
                while (!Arrays.asList(chordNotesString).contains(new SimpleNote(tenor[x], k).getNote().toLowerCase())) {
                    tenor[x] = new SimpleNote(tenor[x], k).getHigherNote();
                }

                alto[x] = alto[x - 1];
                while (!Arrays.asList(chordNotesString).contains(new SimpleNote(alto[x], k).getNote().toLowerCase())) {
                    alto[x] = new SimpleNote(alto[x], k).getHigherNote();
                }

                soprano[x] = soprano[x - 1];
                while (!Arrays.asList(chordNotesString).contains(new SimpleNote(soprano[x], k).getNote().toLowerCase())) {
                    soprano[x] = new SimpleNote(soprano[x], k).getHigherNote();
                }
            }

            if (jump == -6 || jump == 3) {
                tenor[x] = tenor[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(tenor[x], k).getNote().toLowerCase())) {
                    tenor[x] = new SimpleNote(tenor[x], k).getLowerNote();
                }

                alto[x] = alto[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(alto[x], k).getNote().toLowerCase())) {
                    alto[x] = new SimpleNote(alto[x], k).getLowerNote();
                }

                soprano[x] = soprano[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(soprano[x], k).getNote().toLowerCase())) {
                    soprano[x] = new SimpleNote(soprano[x], k).getLowerNote();
                }
            }
            if (jump == 6 || jump == -3) {
                tenor[x] = tenor[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(tenor[x], k).getNote().toLowerCase())) {
                    tenor[x] = new SimpleNote(tenor[x], k).getHigherNote();
                }

                alto[x] = alto[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(alto[x], k).getNote().toLowerCase())) {
                    alto[x] = new SimpleNote(alto[x], k).getHigherNote();
                }

                soprano[x] = soprano[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(soprano[x], k).getNote().toLowerCase())) {
                    soprano[x] = new SimpleNote(soprano[x], k).getHigherNote();
                }
            }

            if (jump == -4 || jump == 5) {
                tenor[x] = tenor[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(tenor[x], k).getNote().toLowerCase())) {
                    tenor[x] = new SimpleNote(tenor[x], k).getLowerNote();
                }

                alto[x] = alto[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(alto[x], k).getNote().toLowerCase())) {
                    alto[x] = new SimpleNote(alto[x], k).getLowerNote();
                }

                soprano[x] = soprano[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(soprano[x], k).getNote().toLowerCase())) {
                    soprano[x] = new SimpleNote(soprano[x], k).getLowerNote();
                }
            }
            if (jump == 4 || jump == -5) {
                tenor[x] = tenor[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(tenor[x], k).getNote().toLowerCase())) {
                    tenor[x] = new SimpleNote(tenor[x], k).getHigherNote();
                }

                alto[x] = alto[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(alto[x], k).getNote().toLowerCase())) {
                    alto[x] = new SimpleNote(alto[x], k).getHigherNote();
                }

                soprano[x] = soprano[x - 1];
                if (!Arrays.asList(chordNotesString).contains(new SimpleNote(soprano[x], k).getNote().toLowerCase())) {
                    soprano[x] = new SimpleNote(soprano[x], k).getHigherNote();
                }
            }
        }
    }

    public void generateChordLengths(int numMeasures)
    {
        List<String> list = new ArrayList<>();
        for (int x = 0; x < numMeasures - 2; x++)
        {
            int beatsLeft = beatsPerMeasure;
            while (beatsLeft > 0)
            {
                if (beatsLeft >= 4)
                {
                    int rand = (int)(Math.random() * 4);
                    if (rand == 0)
                    {
                        list.add("q");
                        beatsLeft -= 1;
                    }
                    if (rand == 1)
                    {
                        list.add("h");
                        beatsLeft -= 2;
                    }
                    if (rand == 2)
                    {
                        list.add("h.");
                        beatsLeft -= 3;
                    }
                    if (rand == 3)
                    {
                        list.add("w");
                        beatsLeft -= 4;
                    }
                }
                else if (beatsLeft == 3)
                {
                    int rand = (int)(Math.random() * 3);
                    if (rand == 0)
                    {
                        list.add("q");
                        beatsLeft -= 1;
                    }
                    if (rand == 1)
                    {
                        list.add("h");
                        beatsLeft -= 2;
                    }
                    if (rand == 2)
                    {
                        list.add("h.");
                        beatsLeft -= 3;
                    }
                }
                else if (beatsLeft == 2)
                {
                    int rand = (int)(Math.random() * 2);
                    if (rand == 0)
                    {
                        list.add("q");
                        beatsLeft -= 1;
                    }
                    if (rand == 1)
                    {
                        list.add("h");
                        beatsLeft -= 2;
                    }
                }
                else
                {
                    list.add("q");
                    beatsLeft -= 1;
                }
            }
        }
        if (beatsPerMeasure == 2) {
            list.add("h");
        }
        else if (beatsPerMeasure == 3) {
            list.add("h");
        }
        else {
            list.add("h");
        }
        chordLengths = list.toArray();
    }

    public void generateVoiceStrings()
    {
        for (int x = 0; x < soprano.length; x++)
        {
            sopranoString += soprano[x] + chordLengths[x] + " ";
        }
        sopranoString += "R" + (beatsPerMeasure == 3 ? "q" : "h") + " ";

        for (int x = 0; x < alto.length; x++)
        {
            altoString += alto[x] + chordLengths[x] + " ";
        }
        altoString += "R" + (beatsPerMeasure == 3 ? "q" : "h") + " ";

        for (int x = 0; x < tenor.length; x++)
        {
            tenorString += tenor[x] + chordLengths[x] + " ";
        }
        tenorString += "R" + (beatsPerMeasure == 3 ? "q" : "h") + " ";

        for (int x = 0; x < bass.length; x++)
        {
            bassString += bass[x] + chordLengths[x] + " ";
        }
        bassString += "R" + (beatsPerMeasure == 3 ? "q" : "h") + " ";
    }

    private String randomRoot()
    {
        int rand = (int)(Math.random() * 3);
        if (rand == 0) return "G";
        if (rand == 1) return "F";
        if (rand == 2) return "C";
        throw new RuntimeException();
    }

    public void generateMusicString()
    {
        int instrument = (int)(Math.random() * sopranoInstruments.length);
        musicString = "TIME:" + beatsPerMeasure + "/4 " + "V0 I" + sopranoInstruments[instrument] + " " + sopranoString + "V1 I" + altoInstruments[instrument] + " " + altoString + "V2 I" + tenorInstruments[instrument] + " " + tenorString + "V3 I" + bassInstruments[instrument] + " " + bassString;
    }

    public void showMusic() {
        StaccatoParser parser = new StaccatoParser();
        MidiParserListener listener = new MidiParserListener();
        parser.addParserListener(listener);
        parser.parse(musicString);
        Sequence sequence = listener.getSequence();
        MidiFileManager fileManager = new MidiFileManager();
        try {
            fileManager.save(sequence, new File("score.mid"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
