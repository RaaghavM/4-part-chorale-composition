package src;

import org.jfugue.theory.*;
import java.util.*;

public class SimpleNote
{
    public int note;
    int octave;
    List<Note> noteList;
    public SimpleNote(String s, Key k)
    {
        octave = Integer.parseInt(s.substring(s.length()-1));
        Intervals i = k.getScale().getIntervals();
        while (!i.getNotes().get(0).toString().substring(0, 1).equals("C"))
        {
            i = i.rotate(1);
        }
        noteList = i.getNotes();
        if (s.substring(0, 1).equals("C")) note = 0;
        if (s.substring(0, 1).equals("D")) note = 1;
        if (s.substring(0, 1).equals("E")) note = 2;
        if (s.substring(0, 1).equals("F")) note = 3;
        if (s.substring(0, 1).equals("G")) note = 4;
        if (s.substring(0, 1).equals("A")) note = 5;
        if (s.substring(0, 1).equals("B")) note = 6;

    }

    public String getHigherNote()
    {
        int oldNote = note;
        note = Math.floorMod(note+1, 7);
        if (note < oldNote) octave++;
        String s = "";
        if (note == 0) s = noteList.get(0).toString().substring(0, noteList.get(0).toString().length() - 1);
        else if (note == 1) s = noteList.get(1).toString().substring(0, noteList.get(1).toString().length() - 1);
        else if (note == 2) s = noteList.get(2).toString().substring(0, noteList.get(2).toString().length() - 1);
        else if (note == 3) s = noteList.get(3).toString().substring(0, noteList.get(3).toString().length() - 1);
        else if (note == 4) s = noteList.get(4).toString().substring(0, noteList.get(4).toString().length() - 1);
        else if (note == 5) s = noteList.get(5).toString().substring(0, noteList.get(5).toString().length() - 1);
        else if (note == 6) s = noteList.get(6).toString().substring(0, noteList.get(6).toString().length() - 1);
        else
        {
            throw new RuntimeException();
        }
        return s + octave;
    }

    public String getLowerNote()
    {
        int oldNote = note;
        note = Math.floorMod(note-1, 7);
        if (note > oldNote) octave--;
        String s = "";
        if (note == 0) s = noteList.get(0).toString().substring(0, noteList.get(0).toString().length() - 1);
        else if (note == 1) s = noteList.get(1).toString().substring(0, noteList.get(1).toString().length() - 1);
        else if (note == 2) s = noteList.get(2).toString().substring(0, noteList.get(2).toString().length() - 1);
        else if (note == 3) s = noteList.get(3).toString().substring(0, noteList.get(3).toString().length() - 1);
        else if (note == 4) s = noteList.get(4).toString().substring(0, noteList.get(4).toString().length() - 1);
        else if (note == 5) s = noteList.get(5).toString().substring(0, noteList.get(5).toString().length() - 1);
        else if (note == 6) s = noteList.get(6).toString().substring(0, noteList.get(6).toString().length() - 1);
        else
        {
            throw new RuntimeException();
        }
        return s + octave;
    }

    public String getNote ()
    {
        String s = "";
        if (note == 0) s = noteList.get(0).toString().substring(0, noteList.get(0).toString().length() - 1);
        else if (note == 1) s = noteList.get(1).toString().substring(0, noteList.get(1).toString().length() - 1);
        else if (note == 2) s = noteList.get(2).toString().substring(0, noteList.get(2).toString().length() - 1);
        else if (note == 3) s = noteList.get(3).toString().substring(0, noteList.get(3).toString().length() - 1);
        else if (note == 4) s = noteList.get(4).toString().substring(0, noteList.get(4).toString().length() - 1);
        else if (note == 5) s = noteList.get(5).toString().substring(0, noteList.get(5).toString().length() - 1);
        else if (note == 6) s = noteList.get(6).toString().substring(0, noteList.get(6).toString().length() - 1);
        else
        {
            throw new RuntimeException();
        }
        return s;
    }

    public int getOctave ()
    {
        return octave;
    }
}
