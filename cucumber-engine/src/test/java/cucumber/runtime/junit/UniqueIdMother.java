package cucumber.runtime.junit;

import org.junit.platform.engine.UniqueId;

class UniqueIdMother {

    static UniqueId anyUniqueId() {
        return UniqueId.forEngine("stand-in");
    }
}
