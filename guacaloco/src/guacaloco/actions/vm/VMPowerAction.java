package guacaloco.actions.vm;

import guacaloco.actions.VMwareEntityAction;
import guacaloco.model.IVMwareEntity;
import guacaloco.model.VirtualMachine;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class VMPowerAction extends VMwareEntityAction implements IMenuCreator {

    private static final String DESCRIPTION = "Power";

    private Menu menu;
    private List<IAction> actions = new ArrayList<IAction>();

    public VMPowerAction(StructuredViewer viewer) {
        super(viewer);
        createActions(viewer);
        setMenuCreator(this);
    }

    @Override
    protected void init() {
        setText(DESCRIPTION);
        setEnabled(true);
    }

    private void createActions(StructuredViewer viewer) {
        actions.add(new VMPowerOnAction(viewer));
        actions.add(new VMPowerOffAction(viewer));
        actions.add(new VMSuspendAction(viewer));
    }

    @Override
    public boolean isValid() {
        IVMwareEntity entity = getViewerSelection();
        return (entity instanceof VirtualMachine);
    }

    @Override
    public void dispose() {
        if (menu != null) {
            menu.dispose();
            menu = null;
        }
    }

    @Override
    public Menu getMenu(Control parent) {
        if (menu == null) {
            menu = new Menu(parent);
            fillMenu(menu);
        }
        return menu;
    }

    @Override
    public Menu getMenu(Menu parent) {
        if (menu == null) {
            menu = new Menu(parent);
            fillMenu(menu);
        }
        return menu;
    }

    private void fillMenu(Menu m) {
        for (IAction layoutAction : actions) {
            ActionContributionItem item = new ActionContributionItem(
                    layoutAction);
            item.fill(m, -1);
        }
    }
}
