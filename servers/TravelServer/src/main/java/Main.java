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
import com.google.firebase.database.ValueEventListener;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		FileInputStream serviceAccount =
				new FileInputStream("travelapp-970a5-firebase-adminsdk-cf7q6-a0f4ec6da1.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
				.setDatabaseUrl("https://travelapp-970a5.firebaseio.com/")
				.build();
		FirebaseApp.initializeApp(options);
		
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
			public void onChildAdded(DataSnapshot snapshot, String key) {
				steps.add(snapshot.getValue(User.class));
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
		
		
	}

}
