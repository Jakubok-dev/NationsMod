package me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment;

import java.util.UUID;
import java.util.function.Supplier;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.governmentElements.DecisiveEntity.DecisiveEntitysVerdict;
import me.jakubok.nationsmod.administration.governmentElements.FormOfGovernment;
import me.jakubok.nationsmod.administration.governmentElements.decisiveEntities.Monarch;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.administration.law.Directive.DirectiveStatus;
import net.minecraft.text.Text;

public class AbsoluteMonarchy<U extends AdministratingUnit<D>, D extends AdministratingUnitLawDescription> extends FormOfGovernment<Monarch, Monarch, U, D> {

    private final Monarch monarch;

    public AbsoluteMonarchy(U administratedUnit,
            Supplier<Directive<D>> directiveFactory) {
        super(administratedUnit, directiveFactory);
        this.monarch = new Monarch(administratedUnit, this);
    }

    @Override
    public Monarch getLegislative() {
        return monarch;
    }
    @Override
    public Monarch getExecutive() {
        return monarch;
    }

    @Override
    public void putUnderDeliberation(Directive<D> directive) {
        directive.status = DirectiveStatus.DELIBERATED_BY_THE_LEGISLATIVE;
        this.getLegislative().putUnderDeliberation(directive.getID());
        this.mapOfDirectives.put(directive.getID(), directive);
    }

    @Override
    public String getName() {
        return "absolute_monarchy";
    }

    @Override
    public Text getDisplayName() {
        return Text.of("Absolute Monarchy");
    }

    @Override
    public Text getDescription() {
        return Text.of("A form of government where one person is having the legislative, executive and the judgemental power.");
    }

    @Override
    public void legislativesVerdictListener(UUID directivesID, DecisiveEntitysVerdict verdict) {
        Directive<D> directive = this.mapOfDirectives.get(directivesID);
        this.mapOfDirectives.remove(directivesID);
        if (verdict == DecisiveEntitysVerdict.APPROVED)
            directive.implement(this.administratedUnit.law);
    }

    @Override
    public void executivesVerdictListener(UUID directivesID, DecisiveEntitysVerdict verdict) {
        this.legislativesVerdictListener(directivesID, verdict);
    }
    
}
