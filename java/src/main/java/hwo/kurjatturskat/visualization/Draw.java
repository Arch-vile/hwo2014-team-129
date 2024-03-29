package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedData;
import hwo.kurjatturskat.model.World;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import org.la4j.vector.Vector;

public class Draw {

    private final static Color LANE_COLOR = Color.BLACK;
    private final static Color SWITCH_COLOR = Color.RED;

    private int OFFSET_X = 10;
    private int OFFSET_Y = 10;
    private double SCALE = 0.5;

    private int maxPlottedYCoord = 0;

    List<TrackLanes> lanes;
    private List<TrackElement> pieces;
    private World world;
    private StringBuffer statusBoard;

    public Draw(List<TrackElement> pieces, World world) {
        this.statusBoard = new StringBuffer();
        this.pieces = pieces;
        this.lanes = Arrays.asList(world.getLanes());
        this.world = world;
    }

    public void paint(Graphics g) {
        this.statusBoard = new StringBuffer();

        int lineIndex = 0;

        for (TrackElement element : pieces) {
            plotTrackPieceIndex(g, lineIndex, element);
            plotTrackPiece(g, element);
            lineIndex++;
        }

        plotCar(g);
        plotStats();

        plotStatusBoard(g);

    }

    public void plotTrackPiece(Graphics g, TrackElement element) {

        g.setColor(LANE_COLOR);
        if (element.getTrackPiece().isSwitch) {
            g.setColor(SWITCH_COLOR);
        }

        if (TrackElement.TYPE_STRAIGHT.equals(element.getType())) {
            drawStraight((Straight) element, g);
        } else {
            drawCurve((Curve) element, g);
        }
    }

    public void plotTrackPieceIndex(Graphics g, int lineIndex,
            TrackElement element) {
        g.drawString("" + lineIndex, adjustX(element.getPosition().get(0)),
                adjustY(element.getPosition().get(1)));
    }

    private void plotStatusBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(15, maxPlottedYCoord + 30, 300, 200);
        g.setColor(Color.GREEN);
        int i = 0;
        for (String line : this.statusBoard.toString().split("\n")) {
            g.drawString(line, 20, maxPlottedYCoord + 45 + i * 15);
            i++;
        }
    }

    public void plotStats() {
        this.statusBoard.append(String.format("%s\n", world.getMyBotName()));
        this.statusBoard.append(String.format("Lap: %s/%s\n", world
                .getLapResults().getLaps().size() + 1, world.getLapResults()
                .getRaceSession().laps));

        this.statusBoard.append(String.format("Throttle: %s\n",
                world.myPhysics.getThrottle()));

        this.statusBoard
                .append(String.format("Speed: %s\n", world.getMySpeed()));
        this.statusBoard.append(String.format("Record speed: %s\n",
                world.getMyRecordSpeed()));

        this.statusBoard.append(String.format("Car angle: %s\n",
                world.myPhysics.getCurrentCarAngle()));

        plotLapTimes();

        this.statusBoard.append("\nTurbo available: "
                + (world.getTurbo() == null ? "NO" : "YES"));
    }

    // TODO: plot -> append
    private void plotLapTimes() {

        for (int i = 0; i < this.world.getLapResults().getRaceSession().laps; i++) {
            if (i < this.world.getLapResults().getLaps().size()) {
                LapFinishedData lapData = this.world.getLapResults().getLaps()
                        .get(i);
                this.statusBoard.append(String.format("Lap %s: %s\n",
                        lapData.lapTime.lap + 1, lapData.lapTime.getLapTime()));
            } else {
                statusBoard.append(String.format("Lap %s: --:--:---\n", i + 1));
            }

        }

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
                drawLine(lastPlot.add(element.getPosition()),
                        nextPlot.add(element.getPosition()), g);
                lastPlot = nextPlot;
            }

        }

    }

    private void drawMarker(double x, double y, Graphics g) {
        g.setColor(Color.red);

        int width = 6;
        g.fillOval(adjustX(x) - width / 2, adjustY(y) - width / 2, width, width);
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

            drawLine(straight.getPosition().get(0) + offSet.get(0), straight
                    .getPosition().get(1) + offSet.get(1), straight
                    .calculateEndPosition().get(0) + offSet.get(0), straight
                    .calculateEndPosition().get(1) + offSet.get(1), g);
        }
    }

    private void drawLine(double x1, double y1, double x2, double y2, Graphics g) {
        int adjustX1 = adjustX(x1);
        int adjustY1 = adjustY(y1);
        int adjustX2 = adjustX(x2);
        int adjustY2 = adjustY(y2);

        g.drawLine(adjustX1, adjustY1, adjustX2, adjustY2);
    }

    private int adjustX(double x) {
        int adjusted = (int) ((x + OFFSET_X) * SCALE);

        if (adjusted < 30) {
            OFFSET_X++;
        }

        return adjusted;
    }

    private int adjustY(double x) {
        int adjusted = (int) ((x + OFFSET_Y) * SCALE);

        if (adjusted < 80) {
            OFFSET_Y++;
        }

        if (adjusted > maxPlottedYCoord) {
            maxPlottedYCoord = adjusted;
        }

        return adjusted;
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

        Vector offSet = VectorMath.rotate(current.getDirection(), 90)
                .normalize().multiply(world.getMyLane().distanceFromCenter);

        Vector direction = current.getDirection();
        Vector carPosition = straightStart.add(offSet).add(
                direction.normalize().multiply(distance));
        drawMarker(carPosition, g);
    }

    private void plotCarOnCurve(Curve current, double distance, Graphics g) {
        double laneDistanceFromCenter = world.getMyLane().distanceFromCenter;

        if (current.getAngle() < 0)
            laneDistanceFromCenter *= -1;

        double wholeCircle = 2 * Math.PI
                * (current.getRadius() - laneDistanceFromCenter);
        double travelledPercentage = distance / wholeCircle;
        double travelledAngle = travelledPercentage * 360;
        if (current.getAngle() < 0)
            travelledAngle *= -1;

        Vector trackStart = current.getRelativeStartPoint().normalize()
                .multiply(current.getRadius() - laneDistanceFromCenter);
        Vector carRelativePos = VectorMath.rotate(trackStart, travelledAngle);
        Vector carPosition = current.getPosition().add(carRelativePos);
        drawMarker(carPosition, g);
    }
}
