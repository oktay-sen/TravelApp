import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.swing.*;

public class Main {

	private static JTextArea jTextArea;

	public static void main(String[] args) throws FileNotFoundException {
		JFrame jFrame = new JFrame();

		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.setSize(640, 480);
		jTextArea = new JTextArea();
		jTextArea.setEditable(false);
		jFrame.add( jTextArea );
		jFrame.setTitle("Travel App Server");
		jFrame.setVisible(true);

		println("Firebase initializing.");
		FileInputStream serviceAccount =
				new FileInputStream("travelapp-970a5-firebase-adminsdk-cf7q6-a0f4ec6da1.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
				.setDatabaseUrl("https://travelapp-970a5.firebaseio.com/")
				.build();
		FirebaseApp.initializeApp(options);
		println("Firebase initialized.");

		DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
		DatabaseReference usersRef = ref.child("users");
		usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onCancelled(DatabaseError arg0) {
				System.out.println(arg0);
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				ArrayList<User> users = new ArrayList<>();
				for (DataSnapshot user : snapshot.getChildren()) {
					println("User found: " + user.getKey());
					users.add(user.getValue(User.class));
				}
			}
		});
		
		ArrayList<User> steps = new ArrayList<>();
		
		usersRef.addChildEventListener(new ChildEventListener() {

			@Override
			public void onCancelled(DatabaseError arg0) {
			}

			@Override
			public void onChildAdded(DataSnapshot snapshot, String fasdghjgk) {
				println("Usersref child added.");
				steps.add(snapshot.getValue(User.class));
				String key = snapshot.getKey();
				FirebaseDatabase.getInstance().getReference("users").child(key).child("footsteps")
				.addChildEventListener(new ChildEventListener() {

					@Override
					public void onCancelled(DatabaseError arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onChildAdded(DataSnapshot arg0, String arg1) {
						Step s = arg0.getValue(Step.class);
						
						
						DatabaseReference ref = FirebaseDatabase.getInstance().getReference("index");
						Query query = ref.child((s.y - 5) + "").startAt(s.x - 5).endAt(s.x + 5);
						query.addListenerForSingleValueEvent(new ValueEventListener() {

							@Override
							public void onCancelled(DatabaseError arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onDataChange(DataSnapshot arg0) {
								if (arg0.getChildrenCount() < 5) {
									DatabaseReference r= FirebaseDatabase.getInstance().getReference();
									DatabaseReference usersR = r.child("places").push();
									Treasure t = new Treasure();
									t.x = 5;
									t.y = 10;
									usersR.setValue(t);
								}
							}
						});
					}

					@Override
					public void onChildChanged(DataSnapshot arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onChildMoved(DataSnapshot arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onChildRemoved(DataSnapshot arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String key) {
				int i = steps.indexOf(snapshot.getValue(User.class));
				steps.set(i, snapshot.getValue(User.class));
			}

			@Override
			public void onChildMoved(DataSnapshot snapshot, String key) {
			}

			@Override
			public void onChildRemoved(DataSnapshot snapshot) {
				steps.remove(snapshot.getValue(User.class));
			}
			
		});
		DatabaseReference r= FirebaseDatabase.getInstance().getReference();
		DatabaseReference usersR = r.child("users").push();
		User us = new User();
		us.username = "Mark";
		usersR.setValue(us);
		
		DatabaseReference x= FirebaseDatabase.getInstance().getReference();
		DatabaseReference u = x.child("places").push();
		Treasure t = new Treasure();
		t.x = 5;
		t.y = 10;
		u.setValue(t);
		
	}

	private static void println(String s) {
		if (jTextArea != null)
			SwingUtilities.invokeLater(() -> jTextArea.append(s + "\n"));
	}

}
