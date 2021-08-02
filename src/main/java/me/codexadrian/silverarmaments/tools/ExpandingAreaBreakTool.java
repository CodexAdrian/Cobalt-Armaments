package me.codexadrian.silverarmaments.tools;

public interface ExpandingAreaBreakTool extends AreaBreakTool{
    default int getExpandedRange() {
        return 2;
    }
}
