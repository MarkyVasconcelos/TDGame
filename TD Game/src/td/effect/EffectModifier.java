package td.effect;

import td.pieces.Monster;

public interface EffectModifier {
	void inflict(Effect effect, Monster target);
}
