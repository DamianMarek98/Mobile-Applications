package zx.opengles.meshes;

public class TexturedPyramidMesh extends BaseMesh {
    public TexturedPyramidMesh() {
        final float[] positionData = {
                //prawa
                1.0f, -1.0f, 1.0f,
                0.0f, 2f, 0.0f,
                -1.0f, -1.0f, 1.0f,

                //tyl
                1.0f, -1.0f, -1.0f,
                0.0f, 2f, 0.0f,
                1.0f, -1.0f, 1.0f,

                //lewa
                -1.0f, -1.0f, -1.0f,
                0.0f, 2f, 0.0f,
                1.0f, -1.0f, -1.0f,

                //przod
                -1.0f, -1.0f, 1.0f,
                0.0f, 2f, 0.0f,
                -1.0f, -1.0f, -1.0f,

                // Dolna ściana
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f
        };

        final float[] texCoordData = {
                //prawa
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,

                //tyl
                0.0f, 0.0f,
                1f, 1.0f,
                1.0f, 0.0f,

                //lewa
                0.0f, 0.0f,
                1f, 1.0f,
                1.0f, 0.0f,

                //przod
                0.0f, 0.0f,
                1f, 1.0f,
                1.0f, 0.0f,

                //podstawa
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        final float[] normalData = {
                //prawa
                1f, 1.0f, 1.0f,
                1f, 1.0f, 1.0f,
                1f, 1.0f, 1.0f,

                //tyl
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,

                //lewa
                0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f,

                //przod
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,

                // Dolna ściana
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };

        numberOfVertices = 18;
        positionBuffer = createBuffer(positionData);
        texCoordsBuffer = createBuffer(texCoordData);
        normalBuffer = createBuffer(normalData);
    }
}
