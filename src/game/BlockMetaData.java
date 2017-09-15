package game;

import java.io.DataOutputStream;

import org.joml.Vector3i;

// Base class for additional metadata. E. g. for chests.
public abstract class BlockMetaData {
	public Vector3i position;
	
	public abstract void save( DataOutputStream out );
}
