package com.eeeab.eeeabsmobs.client.model.entity;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;

public abstract class EMAdvancedEntityModel<T extends EEEABMobEntity & IAnimatedEntity> extends AdvancedEntityModel<T> {

    @Override
    public abstract void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    //角度转换弧度
    protected static float toRadians(double degree) {
        return (float) degree * ((float) Math.PI / 180F);
    }

    //偏移与旋转ModelBox
    protected void offsetAndRotation(AdvancedModelBox box, float px, float py, float pz, float rx, float ry, float rz) {
        box.setRotationPoint(px, py, pz);
        this.setRotationAngle(box, rx, ry, rz);
    }

    /**
     * 起始ModelBox角度(在构造器中使用)
     *
     * @param box 指定模型框
     * @param rx  弧度x
     * @param ry  弧度y
     * @param rz  弧度z
     */
    protected void setRotationAngle(AdvancedModelBox box, float rx, float ry, float rz) {
        box.rotateAngleX = rx;
        box.rotateAngleY = ry;
        box.rotateAngleZ = rz;
    }

    /**
     * ModelBox固定旋转坐标
     *
     * @param box 指定模型框
     * @param px  坐标x
     * @param py  坐标y
     * @param pz  坐标z
     */
    protected void setStaticRotationPoint(AdvancedModelBox box, float px, float py, float pz) {
        box.rotationPointX += px;
        box.rotationPointY += py;
        box.rotationPointZ += pz;
    }

    /**
     * ModelBox固定旋转角度
     *
     * @param box 指定模型框
     * @param rx  弧度x
     * @param ry  弧度y
     * @param rz  弧度z
     */
    protected void setStaticRotationAngle(AdvancedModelBox box, float rx, float ry, float rz) {
        box.rotateAngleX += rx;
        box.rotateAngleY += ry;
        box.rotateAngleZ += rz;
    }
}
