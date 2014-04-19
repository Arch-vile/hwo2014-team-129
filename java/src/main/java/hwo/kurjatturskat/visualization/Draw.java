package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.model.World;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import org.la4j.vector.Vector;

public class Draw {

    private final int OFFSET_X = 300;
    private final int OFFSET_Y = 400;
    private final double SCALE = 1;

    List<TrackLanes> lanes;
    private List<TrackElement> pieces;
    private World world;

    public Draw(List<TrackElement> pieces, World world) {
        this.pieces = pieces;
        this.lanes = Arrays.asList(world.getLanes());
        this.world = world;
    }

    public void paint(Graphics g) {

        int lineIndex = 0;

        for (TrackElement element : pieces) {

            g.drawString("" + lineIndex, adjustX(element.getPosition().get(0)),
                    adjustY(element.getPosition().get(1)));

            if (TrackElement.TYPE_STRAIGHT.equals(element.getType())) {
                drawStraight((Straight) element, g);
            } else {
                drawCurve((Curve) element, g);
            }

            lineIndex++;
        }

        plotCar(g);

    }

    private void drawCurve(Curve element, Graphics g) {

        double right = 1;
        if (element.getAngle() < 0)
            right = -1;

        for (TrackLanes lane : this.lanes) {

            Vector startPlot = element.getRelativeStartPoint().normalize()
                    .multiply(element.getRadius() + lane.distanceFromCenter);
            Vector lastPlot = startPlot;
            for (double angle = 5; angle <= Math.abs(element.getAngle()); angle += 5) {
                Vector nextPlot = VectorMath.rotate(startPlot, angle * right);
                drawLine(lastPlot.add(element.position),
                        nextPlot.add(element.position), g);
                lastPlot = nextPlot;
            }

        }

    }

    private void drawMarker(double x, double y, Graphics g) {
        g.setColor(Color.red);
        g.fillOval(adjustX(x), adjustY(y), 6, 6);
    }

    private void drawMarker(Vector point, Graphics g) {
        drawMarker(point.get(0), point.get(1), g);
    }

    private void drawLine(Vector currentPlot, Vector nextPlot, Graphics g) {
        drawLine(currentPlot.get(0), currentPlot.get(1), nextPlot.get(0),
                nextPlot.get(1), g);

    }

    private void drawStraight(Straight straight, Graphics g) {
        for (TrackLanes lane : this.lanes) {

            Vector offSet = VectorMath.rotate(straight.getDirection(), 90)
                    .normalize().multiply(lane.distanceFromCenter);

            drawLine(straight.position.get(0) + offSet.get(0),
                    straight.position.get(1) + offSet.get(1), straight
                            .calculateEndPosition().get(0) + offSet.get(0),
                    straight.calculateEndPosition().get(1) + offSet.get(1), g);
        }
    }

    private void drawLine(double x1, double y1, double x2, double y2, Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(adjustX(x1), adjustY(y1), adjustX(x2), adjustY(y2));
    }

    private int adjustX(double x) {
        return (int) ((x + OFFSET_X) * SCALE);
    }

    private int adjustY(double x) {
        return (int) ((x + OFFSET_Y) * SCALE);
    }

    public void plotCar(Graphics g) {
        int pieceIndex = this.world.getPreviousPosition().pieceIndex;
        double distance = this.world.getPreviousPosition().inPieceDistance;
        TrackElement trackElement = pieces.get(pieceIndex);
        if (trackElement.isCurve()) {
            plotCarOnCurve((Curve) trackElement, distance, g);
        }

        if (trackElement.isStraight()) {
            plotCarOnStraight((Straight) trackElement, distance, g);
        }
    }

    private void plotCarOnStraight(Straight current, double distance, Graphics g) {
        Vector straightStart = current.getPosition();
        Vector direction = current.getDirection();
        Vector carPosition = straightStart.add(direction.normalize().multiply(
                distance));
        drawMarker(carPosition, g);
    }

    private void plotCarOnCurve(Curve current, double distance, Graphics g) {
        double wholeCircle = 2 * Math.PI * current.getRadius();
        double travelledPercentage = distance / wholeCircle;
        double travelledAngle = travelledPercentage * 360;
        if (current.getAngle() < 0)
            travelledAngle *= -1;

        Vector trackStart = current.getRelativeStartPoint();
        Vector carRelativePos = VectorMath.rotate(trackStart, travelledAngle);
        Vector carPosition = current.getPosition().add(carRelativePos);
        drawMarker(carPosition, g);
    }
}
