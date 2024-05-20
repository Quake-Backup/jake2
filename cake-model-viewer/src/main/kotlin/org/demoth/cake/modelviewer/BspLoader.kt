package org.demoth.cake.modelviewer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.GL20.GL_TRIANGLES
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import jake2.qcommon.filesystem.Bsp
import java.io.File
import java.nio.ByteBuffer

class BspLoader {
    fun loadBspModelTextured(file: File): ModelInstance {
        val bsp = Bsp(ByteBuffer.wrap(file.readBytes()))

        val modelBuilder = ModelBuilder()
        modelBuilder.begin()
        val texture = Texture(Gdx.files.internal("tile1.png"))
        val meshBuilder = modelBuilder.part(
            "part1",
            GL_TRIANGLES,
            VertexAttributes(VertexAttribute.Position(), VertexAttribute.TexCoords(0)),
            Material(
                TextureAttribute(
                    TextureAttribute.Diffuse,
                    texture,
                )
            )
        )

        val vertexBuffer = bsp.faces.forEach { f ->
            val edgeIndices = (0..<f.numEdges).map { edgeIndex ->
                bsp.faceEdges[f.firstEdgeIndex + edgeIndex]
            }

            // list of vertex indices in clockwise order, forming a triangle fan
            val vertices = edgeIndices.map { edgeIndex ->
                if (edgeIndex > 0) {
                    val edge = bsp.edges[edgeIndex]
                    edge.v2
                } else {
                    val edge = bsp.edges[-edgeIndex]
                    edge.v1
                }
            }
            val textureInfo = bsp.textures[f.textureInfoIndex]


            val vertexBuffer = vertices.drop(1).windowed(2).flatMap { (i1, i2) ->
                val v0 = bsp.vertices[vertices.first()]
                val v1 = bsp.vertices[i1]
                val v2 = bsp.vertices[i2]
                val uv0 = textureInfo.calculateUV(v0, 1024, 1024)
                val uv1 = textureInfo.calculateUV(v1, 1024, 1024)
                val uv2 = textureInfo.calculateUV(v2, 1024, 1024)
                listOf(
                    v2.x, v2.y, v2.z, uv2.first(), uv2.last(),
                    v1.x, v1.y, v1.z, uv1.first(), uv1.last(),
                    v0.x, v0.y, v0.z, uv0.first(), uv0.last(),
                    )
            }
            val size = vertexBuffer.size / 5 // 5 floats per vertex : fixme: not great
            meshBuilder.addMesh(vertexBuffer.toFloatArray(), (0..<size).map { it.toShort() }.toShortArray())
        }

        val model = modelBuilder.end()
        return ModelInstance(model)
    }


    fun loadBSPModelWireFrame(file: File): ModelInstance {
        val bsp = Bsp(ByteBuffer.wrap(file.readBytes()))

        val modelBuilder = ModelBuilder()
        modelBuilder.begin()
        val partBuilder: MeshPartBuilder = modelBuilder.part(
            "lines",
            GL20.GL_LINES,
            (Usage.Position or Usage.ColorUnpacked).toLong(),
            Material(ColorAttribute.createDiffuse(Color.WHITE))
        )
        bsp.edges.forEach {
            val from = bsp.vertices[it.v1]
            val to = bsp.vertices[it.v2]
            partBuilder.line(
                from.x,
                from.y,
                from.z,
                to.x,
                to.y,
                to.z
            )
        }
        return ModelInstance(modelBuilder.end())

    }
}