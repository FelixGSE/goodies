package goodies.agents

import goodies.geometry.Point
import goodies.geometry.Polygon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AgentTest {
    private val boundary =
        Polygon(
            Point(0f, 0f),
            Point(100f, 0f),
            Point(100f, 100f),
            Point(0f, 100f),
        )

    @Nested
    inner class Construction {
        @Test
        fun `Agent should hold the boundary when created with one`() {
            // Given

            // When
            val agent = Agent(boundary)

            // Then
            assertThat(agent.boundary).isEqualTo(boundary)
            assertThat(agent.dir).isEqualTo(1)
        }

        @Test
        fun `Agent should start inside the boundary`() {
            // Given

            // When
            val agent = Agent(boundary)

            // Then - the random point should be somewhere near the polygon
            assertThat(agent.pos).isNotNull()
        }

        @Test
        fun `Agent should take the explicit position when one is given`() {
            // Given

            // When
            val agent = Agent(pos = Point(50f, 50f), boundary = boundary)

            // Then
            assertThat(agent.pos.x).isEqualTo(50f)
            assertThat(agent.pos.y).isEqualTo(50f)
        }

        @Test
        fun `Agent should have a default size and speed`() {
            // Given

            // When
            val agent = Agent(boundary)

            // Then
            assertThat(agent.size).isBetween(5f, 50f)
            assertThat(agent.speed).isBetween(10f, 20f)
        }
    }
}
