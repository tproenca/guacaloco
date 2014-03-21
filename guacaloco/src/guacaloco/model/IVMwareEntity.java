package guacaloco.model;

public interface IVMwareEntity {
    IVMwareEntity getParent();
    IVMwareEntity[] getChildren();
    String getName();
}
