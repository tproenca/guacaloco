package guacaloco.actions;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class SnippetAction extends VMwareEntityAction {

    public SnippetAction(StructuredViewer viewer) {
        super(viewer);
    }

    public SnippetAction(StructuredViewer viewer, int style) {
        super(viewer, style);
    }

    @Override
    public void run() {
        try {
            IEditorPart part = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (part instanceof ITextEditor) {
                final ITextEditor editor = (ITextEditor) part;
                IDocumentProvider prov = editor.getDocumentProvider();
                IDocument doc = prov.getDocument(editor.getEditorInput());
                ISelection sel = editor.getSelectionProvider().getSelection();
                if (sel instanceof TextSelection) {
                    final TextSelection textSel = (TextSelection) sel;
                    doc.replace(textSel.getOffset(), textSel.getLength(),
                            getSnippet());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract String getSnippet();
}
