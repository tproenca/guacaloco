package guacaloco.core.utils;

import guacaloco.core.VsphereToolkitException;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class AddSampleIntoEclipseProject {

    public static IPackageFragment createPackage(String packageName) throws VsphereToolkitException {
        IPackageFragment pack = null;
        try {
            IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject project = workspaceRoot.getProjects()[0]; // FIXME: get active project
            IFolder sourceFolder = project.getFolder("src"); // FIXME: use getSourceFolders()
            IJavaProject javaProject = JavaCore.create(project);
            pack = javaProject.getPackageFragmentRoot(sourceFolder).createPackageFragment(
                    packageName, false, null);
        } catch (JavaModelException e) {
            e.printStackTrace();
            throw new VsphereToolkitException("Unable to create package", e.getCause());
        }
        return pack;
    }

    public static ICompilationUnit createJavaClass(IPackageFragment pack, String textContent,
            String fileName) throws VsphereToolkitException {
        ICompilationUnit klass = getClassInPackage(pack, fileName);
        if (klass == null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(textContent);
            try {
                klass = pack.createCompilationUnit(fileName, buffer.toString(), false, null);
            } catch (JavaModelException e) {
                e.printStackTrace();
                throw new VsphereToolkitException("Unable to create class", e.getCause());
            }
        }
        return klass;
    }

    private static ICompilationUnit getClassInPackage(IPackageFragment pack, String fileName)
            throws VsphereToolkitException {
        try {
            ICompilationUnit[] klasses = pack.getCompilationUnits();
            for (ICompilationUnit klass : klasses) {
                if (fileName.equals(klass.getElementName())) {
                    return klass;
                }
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
            throw new VsphereToolkitException("Unable to find class", e.getCause());
        }
        return null;
    }

    @SuppressWarnings("unused")
    private static String[] getSourceFolders(IJavaProject project) throws VsphereToolkitException {
        List<String> result = new LinkedList<String>();
        try {
            IClasspathEntry[] classPaths = project.getRawClasspath();
            for (IClasspathEntry cp : classPaths) {
                if (cp.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    result.add(cp.getPath().toPortableString());
                }
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
            throw new VsphereToolkitException("Unable to get source folder", e.getCause());
        }
        return result.toArray(new String[0]);
    }

}
