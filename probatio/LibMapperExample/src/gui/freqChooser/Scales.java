package gui.freqChooser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfugue.provider.NoteProvider;
import org.jfugue.provider.NoteProviderFactory;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Intervals;
import org.jfugue.theory.Note;
import org.jfugue.theory.Scale;

public class Scales {
	
	public static Map<String, String> intervalsString;
	public static Map<String, Scale> scales;
	public static Map<String, Intervals> intervals;
	
	static{
		intervalsString = new HashMap<String, String>();
		intervalsString.put("Bebop", "1 2 3 4 5 #5 6 7 8");
		intervalsString.put("Bebop Dorian", "1 2 b3 4 5 6 b7 7 8");
		intervalsString.put("Bebop Dominant", "1 2 3 4 5 6 b7 7 8");
		intervalsString.put("Spanish 8 Tone", "1 b2 b3 3 4 b5 #5 b7 8");
		intervalsString.put("Ionian", "1 2 3 4 5 6 7 8");
		intervalsString.put("Dorian", "1 2 b3 4 5 6 b7 8");
		intervalsString.put("Phrygian", "1 b2 b3 4 5 b6 b7 8");
		intervalsString.put("Lydian", "1 2 3 #4 5 6 7 8");
		intervalsString.put("Mixolydian", "1 2 3 4 5 6 b7 8");
		intervalsString.put("Aeolian", "1 2 b3 4 5 b6 b7 8");
		intervalsString.put("Locrian", "1 b2 b3 4 b5 b6 b7 8");
		intervalsString.put("Whole Tone", "1 2 3 b5 #5 b7 8");
		intervalsString.put("Augmented", "1 b3 3 5 #5 7 8");
		intervalsString.put("Pelog", "1 b2 b3 4 5 b7 8");
		intervalsString.put("Mystic", "1 2 3 b5 6 b7 8");
		intervalsString.put("In Sen", "1 b2 4 5 b7 8");
		intervalsString.put("Iwato", "1 b2 4 b5 b7 8");
		intervalsString.put("Scottish", "1 2 4 5 6 8");
		intervalsString.put("Kumoi", "1 b2 4 5 #5 8");
		intervalsString.put("Egyptian", "1 2 4 5 b7 8");
		intervalsString.put("Yo", "1 b3 4 5 b7 8");
		intervalsString.put("Hirojoshi", "1 2 b3 5 #5 8");
		intervalsString.put("Balinese", "1 b2 b3 5 #5 8");
		intervalsString.put("Mongolian", "1 2 3 5 6 8");
		intervalsString.put("Ryo", "1 2 3 5 6 8");
		intervalsString.put("Natural Minor", "1 2 b3 4 5 #5 b7 8");
		intervalsString.put("Gypsy Minor", "1 2 b3 b5 5 #5 b7 8");
		intervalsString.put("Javanese", "1 b2 b3 4 5 6 b7 8");
		intervalsString.put("Neapolitan Minor", "1 b2 b3 4 5 #5 7 8");
		intervalsString.put("Neapolitan Major", "1 b2 b3 4 5 6 7 8");
		intervalsString.put("Hungarian Minor", "1 2 b3 b5 5 #5 7 8");
		intervalsString.put("Overtone", "1 2 3 b5 5 #5 b7 8");
		intervalsString.put("Hindu", "1 2 3 4 5 #5 b7 8");
		intervalsString.put("Hungarian Major", "1 b3 3 b5 5 6 b7 8");
		intervalsString.put("Spanish Gypsy", "1 b2 3 4 5 #5 b7 8");
		intervalsString.put("Arabian", "1 2 3 4 b5 #5 b7 8");
		intervalsString.put("Oriental", "1 b2 3 4 b5 6 b7 8");
		intervalsString.put("Major Scale", "1 2 3 4 5 6 7 8");
		intervalsString.put("Hungarian Folk", "1 b2 3 5 b6 7 8");
		intervalsString.put("Gypsy", "1 b2 3 4 5 #5 7 8");
		intervalsString.put("Byzantine", "1 b2 3 4 5 #5 7 8");
		intervalsString.put("Leading Tone", "1 2 3 b5 #5 b7 7 8");
		intervalsString.put("Enigmatic", "1 b2 3 b5 #5 b7 7 8");
		intervalsString.put("Persian", "1 b2 3 4 b5 #5 7 8");
		intervalsString.put("Melodic Minor", "1 2 b3 4 5 6 7 8");
		intervalsString.put("Harmonic Minor", "1 2 b3 4 5 b6 7 8");
		intervalsString.put("Diminished", "1 2 b3 4 b5 b6 6 7 8");
		intervalsString.put("Blues", "1 b3 4 b5 5 b7 8");
		intervalsString.put("Minor Pentatonic", "1 b3 4 5 b7 8");
		intervalsString.put("Major Pentatonic", "1 2 3 5 6 8");
		scales = new HashMap<String, Scale>();
		scales.put("Bebop", new Scale(new Intervals( "1 2 3 4 5 #5 6 7 8")));
		scales.put("Bebop Dorian", new Scale(new Intervals( "1 2 b3 4 5 6 b7 7 8")));
		scales.put("Bebop Dominant", new Scale(new Intervals( "1 2 3 4 5 6 b7 7 8")));
		scales.put("Spanish 8 Tone", new Scale(new Intervals( "1 b2 b3 3 4 b5 #5 b7 8")));
		scales.put("Ionian", new Scale(new Intervals( "1 2 3 4 5 6 7 8")));
		scales.put("Dorian", new Scale(new Intervals( "1 2 b3 4 5 6 b7 8")));
		scales.put("Phrygian", new Scale(new Intervals( "1 b2 b3 4 5 b6 b7 8")));
		scales.put("Lydian", new Scale(new Intervals( "1 2 3 #4 5 6 7 8")));
		scales.put("Mixolydian", new Scale(new Intervals( "1 2 3 4 5 6 b7 8")));
		scales.put("Aeolian", new Scale(new Intervals( "1 2 b3 4 5 b6 b7 8")));
		scales.put("Locrian", new Scale(new Intervals( "1 b2 b3 4 b5 b6 b7 8")));
		scales.put("Whole Tone", new Scale(new Intervals( "1 2 3 b5 #5 b7 8")));
		scales.put("Augmented", new Scale(new Intervals( "1 b3 3 5 #5 7 8")));
		scales.put("Pelog", new Scale(new Intervals( "1 b2 b3 4 5 b7 8")));
		scales.put("Mystic", new Scale(new Intervals( "1 2 3 b5 6 b7 8")));
		scales.put("In Sen", new Scale(new Intervals( "1 b2 4 5 b7 8")));
		scales.put("Iwato", new Scale(new Intervals( "1 b2 4 b5 b7 8")));
		scales.put("Scottish", new Scale(new Intervals( "1 2 4 5 6 8")));
		scales.put("Kumoi", new Scale(new Intervals( "1 b2 4 5 #5 8")));
		scales.put("Egyptian", new Scale(new Intervals( "1 2 4 5 b7 8")));
		scales.put("Yo", new Scale(new Intervals( "1 b3 4 5 b7 8")));
		scales.put("Hirojoshi", new Scale(new Intervals( "1 2 b3 5 #5 8")));
		scales.put("Balinese", new Scale(new Intervals( "1 b2 b3 5 #5 8")));
		scales.put("Mongolian", new Scale(new Intervals( "1 2 3 5 6 8")));
		scales.put("Ryo", new Scale(new Intervals( "1 2 3 5 6 8")));
		scales.put("Natural Minor", new Scale(new Intervals( "1 2 b3 4 5 #5 b7 8")));
		scales.put("Gypsy Minor", new Scale(new Intervals( "1 2 b3 b5 5 #5 b7 8")));
		scales.put("Javanese", new Scale(new Intervals( "1 b2 b3 4 5 6 b7 8")));
		scales.put("Neapolitan Minor", new Scale(new Intervals( "1 b2 b3 4 5 #5 7 8")));
		scales.put("Neapolitan Major", new Scale(new Intervals( "1 b2 b3 4 5 6 7 8")));
		scales.put("Hungarian Minor", new Scale(new Intervals( "1 2 b3 b5 5 #5 7 8")));
		scales.put("Overtone", new Scale(new Intervals( "1 2 3 b5 5 #5 b7 8")));
		scales.put("Hindu", new Scale(new Intervals( "1 2 3 4 5 #5 b7 8")));
		scales.put("Hungarian Major", new Scale(new Intervals( "1 b3 3 b5 5 6 b7 8")));
		scales.put("Spanish Gypsy", new Scale(new Intervals( "1 b2 3 4 5 #5 b7 8")));
		scales.put("Arabian", new Scale(new Intervals( "1 2 3 4 b5 #5 b7 8")));
		scales.put("Oriental", new Scale(new Intervals( "1 b2 3 4 b5 6 b7 8")));
		scales.put("Major Scale", new Scale(new Intervals( "1 2 3 4 5 6 7 8")));
		scales.put("Hungarian Folk", new Scale(new Intervals( "1 b2 3 5 b6 7 8")));
		scales.put("Gypsy", new Scale(new Intervals( "1 b2 3 4 5 #5 7 8")));
		scales.put("Byzantine", new Scale(new Intervals( "1 b2 3 4 5 #5 7 8")));
		scales.put("Leading Tone", new Scale(new Intervals( "1 2 3 b5 #5 b7 7 8")));
		scales.put("Enigmatic", new Scale(new Intervals( "1 b2 3 b5 #5 b7 7 8")));
		scales.put("Persian", new Scale(new Intervals( "1 b2 3 4 b5 #5 7 8")));
		scales.put("Melodic Minor", new Scale(new Intervals( "1 2 b3 4 5 6 7 8")));
		scales.put("Harmonic Minor", new Scale(new Intervals( "1 2 b3 4 5 b6 7 8")));
		scales.put("Diminished", new Scale(new Intervals( "1 2 b3 4 b5 b6 6 7 8")));
		scales.put("Blues", new Scale(new Intervals( "1 b3 4 b5 5 b7 8")));
		scales.put("Minor Pentatonic", new Scale(new Intervals( "1 b3 4 5 b7 8")));
		scales.put("Major Pentatonic", new Scale(new Intervals( "1 2 3 5 6 8")));
		intervals = new HashMap<String, Intervals>();
		intervals.put("Bebop", new Intervals( "1 2 3 4 5 #5 6 7 8"));
		intervals.put("Bebop Dorian", new Intervals( "1 2 b3 4 5 6 b7 7 8"));
		intervals.put("Bebop Dominant", new Intervals( "1 2 3 4 5 6 b7 7 8"));
		intervals.put("Spanish 8 Tone", new Intervals( "1 b2 b3 3 4 b5 #5 b7 8"));
		intervals.put("Ionian", new Intervals( "1 2 3 4 5 6 7 8"));
		intervals.put("Dorian", new Intervals( "1 2 b3 4 5 6 b7 8"));
		intervals.put("Phrygian", new Intervals( "1 b2 b3 4 5 b6 b7 8"));
		intervals.put("Lydian", new Intervals( "1 2 3 #4 5 6 7 8"));
		intervals.put("Mixolydian", new Intervals( "1 2 3 4 5 6 b7 8"));
		intervals.put("Aeolian", new Intervals( "1 2 b3 4 5 b6 b7 8"));
		intervals.put("Locrian", new Intervals( "1 b2 b3 4 b5 b6 b7 8"));
		intervals.put("Whole Tone", new Intervals( "1 2 3 b5 #5 b7 8"));
		intervals.put("Augmented", new Intervals( "1 b3 3 5 #5 7 8"));
		intervals.put("Pelog", new Intervals( "1 b2 b3 4 5 b7 8"));
		intervals.put("Mystic", new Intervals( "1 2 3 b5 6 b7 8"));
		intervals.put("In Sen", new Intervals( "1 b2 4 5 b7 8"));
		intervals.put("Iwato", new Intervals( "1 b2 4 b5 b7 8"));
		intervals.put("Scottish", new Intervals( "1 2 4 5 6 8"));
		intervals.put("Kumoi", new Intervals( "1 b2 4 5 #5 8"));
		intervals.put("Egyptian", new Intervals( "1 2 4 5 b7 8"));
		intervals.put("Yo", new Intervals( "1 b3 4 5 b7 8"));
		intervals.put("Hirojoshi", new Intervals( "1 2 b3 5 #5 8"));
		intervals.put("Balinese", new Intervals( "1 b2 b3 5 #5 8"));
		intervals.put("Mongolian", new Intervals( "1 2 3 5 6 8"));
		intervals.put("Ryo", new Intervals( "1 2 3 5 6 8"));
		intervals.put("Natural Minor", new Intervals( "1 2 b3 4 5 #5 b7 8"));
		intervals.put("Gypsy Minor", new Intervals( "1 2 b3 b5 5 #5 b7 8"));
		intervals.put("Javanese", new Intervals( "1 b2 b3 4 5 6 b7 8"));
		intervals.put("Neapolitan Minor", new Intervals( "1 b2 b3 4 5 #5 7 8"));
		intervals.put("Neapolitan Major", new Intervals( "1 b2 b3 4 5 6 7 8"));
		intervals.put("Hungarian Minor", new Intervals( "1 2 b3 b5 5 #5 7 8"));
		intervals.put("Overtone", new Intervals( "1 2 3 b5 5 #5 b7 8"));
		intervals.put("Hindu", new Intervals( "1 2 3 4 5 #5 b7 8"));
		intervals.put("Hungarian Major", new Intervals( "1 b3 3 b5 5 6 b7 8"));
		intervals.put("Spanish Gypsy", new Intervals( "1 b2 3 4 5 #5 b7 8"));
		intervals.put("Arabian", new Intervals( "1 2 3 4 b5 #5 b7 8"));
		intervals.put("Oriental", new Intervals( "1 b2 3 4 b5 6 b7 8"));
		intervals.put("Major Scale", new Intervals( "1 2 3 4 5 6 7 8"));
		intervals.put("Hungarian Folk", new Intervals( "1 b2 3 5 b6 7 8"));
		intervals.put("Gypsy", new Intervals( "1 b2 3 4 5 #5 7 8"));
		intervals.put("Byzantine", new Intervals( "1 b2 3 4 5 #5 7 8"));
		intervals.put("Leading Tone", new Intervals( "1 2 3 b5 #5 b7 7 8"));
		intervals.put("Enigmatic", new Intervals( "1 b2 3 b5 #5 b7 7 8"));
		intervals.put("Persian", new Intervals( "1 b2 3 4 b5 #5 7 8"));
		intervals.put("Melodic Minor", new Intervals( "1 2 b3 4 5 6 7 8"));
		intervals.put("Harmonic Minor", new Intervals( "1 2 b3 4 5 b6 7 8"));
		intervals.put("Diminished", new Intervals( "1 2 b3 4 b5 b6 6 7 8"));
		intervals.put("Blues", new Intervals( "1 b3 4 b5 5 b7 8"));
		intervals.put("Minor Pentatonic", new Intervals( "1 b3 4 5 b7 8"));
		intervals.put("Major Pentatonic", new Intervals( "1 2 3 5 6 8"));
	}
	
	public static Intervals getIntervalByName(String name){
		return intervals.get(name);
	}
	
	public static List<String> getScalesNames(){
		return new ArrayList<String>(intervals.keySet());
	}
	
	public static Note[] getNotesFromRootAndIntervalName(byte root, String name){
		Note rootNote = new Note(root);
		Chord chord = new Chord(rootNote, getIntervalByName(name));
		return chord.getNotes();
	}

}
