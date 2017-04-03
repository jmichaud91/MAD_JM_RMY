package arbreDecision;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Marc-André on 2017-03-20.
 */
public class TreeLeaf {

    private String value;
    
    public TreeLeaf(String value) {
        this.value = value;
    }

	public String getValue() {
		return value;
	}
}
