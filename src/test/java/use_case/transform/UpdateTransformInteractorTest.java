package use_case.transform;

import entity.GameObject;
import entity.Transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UpdateTransformInteractorTest {

    private static class SpyPresenter implements UpdateTransformOutputBoundary {
        UpdateTransformOutputData lastData = null;
        int callCount = 0;

        @Override
        public void presentTransform(UpdateTransformOutputData data) {
            this.lastData = data;
            this.callCount++;
        }
    }

    @Test
    void updateTransform_doesNothing_whenGameObjectIsNull() {
        SpyPresenter presenter = new SpyPresenter();
        UpdateTransformInteractor interactor =
                new UpdateTransformInteractor(null, presenter);

        UpdateTransformInputData input =
                new UpdateTransformInputData(10.0, 20.0, 2.0, 45.0f);

        interactor.updateTransform(input);

        assertEquals(0, presenter.callCount);
        assertNull(presenter.lastData);
    }

    @Test
    void updateTransform_doesNothing_whenTransformIsNull() {
        SpyPresenter presenter = new SpyPresenter();

        GameObject gameObject = new GameObject(
                "id-1",
                "TestObject",
                true,
                null,
                null,
                null,
                null
        );

        UpdateTransformInteractor interactor =
                new UpdateTransformInteractor(gameObject, presenter);

        UpdateTransformInputData input =
                new UpdateTransformInputData(10.0, 20.0, 2.0, 45.0f);

        interactor.updateTransform(input);

        assertEquals(0, presenter.callCount);
        assertNull(presenter.lastData);
        assertNull(gameObject.getTransform());
    }

    @Test
    void updateTransform_updatesTransformAndCallsPresenter() {
        SpyPresenter presenter = new SpyPresenter();

        // initial transform values
        Transform initialTransform;
        initialTransform = new Transform(
                0.0,
                0.0,
                0.0f,
                1.0,
                1.0
        );

        GameObject gameObject = new GameObject(
                "id-2",
                "TestObject2",
                true,
                null,
                initialTransform,
                null,
                null
        );

        UpdateTransformInteractor interactor =
                new UpdateTransformInteractor(gameObject, presenter);

        double newX = 5.5;
        double newY = -3.25;
        double newScale = 2.0;
        float newRotation = 30.0f;

        UpdateTransformInputData input =
                new UpdateTransformInputData(newX, newY, newScale, newRotation);

        interactor.updateTransform(input);

        Transform t = gameObject.getTransform();
        assertNotNull(t);
        assertEquals(newX, t.getX());
        assertEquals(newY, t.getY());
        assertEquals(newScale, t.getScaleX());
        assertEquals(newScale, t.getScaleY());
        assertEquals(newRotation, t.getRotation());

        assertEquals(1, presenter.callCount);
        assertNotNull(presenter.lastData);

        UpdateTransformOutputData out = presenter.lastData;
        assertEquals(newX, out.getX());
        assertEquals(newY, out.getY());
        assertEquals(newScale, out.getScale());
        assertEquals(newRotation, out.getRotation());
    }
}
