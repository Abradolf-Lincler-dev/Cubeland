package game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3i;

// Base class for additional metadata. E. g. for chests.
public abstract class BlockMetaData {
	String type;
	public Vector3i position;

	public final void save( DataOutputStream out ) throws IOException {
		out.writeUTF( type );
		out.writeInt(position.x);
		out.writeInt(position.y);
		out.writeInt(position.z);
		saveImpl(out);
	}
	public final BlockMetaData load( DataInputStream in, String type ) throws IOException {
		Vector3i tmpPosition = new Vector3i();
		tmpPosition.x = in.readInt();
		tmpPosition.y = in.readInt();
		tmpPosition.z = in.readInt();
		BlockMetaData tmp = loadNew(in);
		tmp.type = type;
		tmp.position = tmpPosition;
		return tmp;
	}
	
	// save additional data to the ostream.
	protected abstract void saveImpl( DataOutputStream out );
	// create a new instance and load its data from a istream.
	protected abstract BlockMetaData loadNew( DataInputStream in );
}
