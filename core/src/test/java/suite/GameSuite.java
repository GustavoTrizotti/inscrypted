package suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("br.edu.ifsp.scl.core")
@SuiteDisplayName("All game rules")
@IncludeTags("game")
public class GameSuite {
}
