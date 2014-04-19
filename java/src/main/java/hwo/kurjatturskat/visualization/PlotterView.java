package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.model.World;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;

import org.la4j.vector.Vector;

public class PlotterView extends JFrame {

    private Graphics bufferGraphics;
    private Image offscreen;
    private Draw draw;
    private Dimension screenDimension = new Dimension(900, 900);

    public PlotterView(World world) {
        List<TrackElement> pieces = TrackElement.convert(world.getTrackModel()
                .getAll());
        positionTrackElements(pieces);
        this.draw = new Draw(pieces, world);

        this.setPreferredSize(screenDimension);
        this.pack();
        this.setVisible(true);
        offscreen = createImage(screenDimension.width, screenDimension.height);
        bufferGraphics = offscreen.getGraphics();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void paint(Graphics g) {
        if (bufferGraphics != null) {
            bufferGraphics.clearRect(0, 0, screenDimension.width,
                    screenDimension.width);
            this.draw.paint(bufferGraphics);
            g.drawImage(offscreen, 0, 0, this);
        }

    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void plot() {
        this.repaint();
    }

    private void positionTrackElements(List<TrackElement> pieces) {

        for (int i = 0; i < pieces.size(); i++) {
            TrackElement current = pieces.get(i);
            TrackElement previous;
            if (i == 0) {
                previous = null;
            } else {
                previous = pieces.get(i - 1);
            }

            locatePiecePosition(previous, current);
            System.out.println(current);
        }

    }

    private void locatePiecePosition(TrackElement previous, TrackElement current) {

        if (current.isCurve()) {
            locateCurvePosition(previous, (Curve) current);
        } else {
            locateStraightPosition(previous, (Straight) current);
        }

    }

    private void locateStraightPosition(TrackElement previous, Straight current) {

        if (previous == null) {
            current.setDirection(VectorMath.zeroDegrees(current.getLength()));
            current.setPosition(VectorMath.zeroVector());
            return;
        }

        if (previous.isCurve()) {
            Curve previousCurve = (Curve) previous;

            double curveRight = 1;
            if (previousCurve.getAngle() < 0)
                curveRight = -1;

            Vector curveRelativeEndPoint = previousCurve
                    .calculateRelativeEndPoint();
            Vector straightDirection = VectorMath
                    .rotate(curveRelativeEndPoint, 90 * curveRight).normalize()
                    .multiply(current.getLength());
            current.setPosition(previousCurve.getPosition().add(
                    curveRelativeEndPoint));
            current.setDirection(straightDirection);
            return;
        }

        if (previous.isStraight()) {
            Straight straight = (Straight) previous;
            Vector startPoint = straight.calculateEndPosition();
            Vector direction = straight.getDirection().normalize()
                    .multiply(current.getLength());
            current.setDirection(direction);
            current.setPosition(startPoint);
        }

    }

    private void locateCurvePosition(TrackElement previous, Curve curve) {

        if (previous == null) {
            curve.setPosition(VectorMath.zeroVector());
        }

        if (previous.isStraight()) {
            Straight straight = (Straight) previous;
            Vector straightDirection = straight.getDirection();

            double curveRight = 1;
            if (curve.getAngle() < 0)
                curveRight = -1;

            Vector fromStraightEndToCircleCenter = VectorMath
                    .rotate(straightDirection, 90 * curveRight).normalize()
                    .multiply(curve.getRadius());

            Vector centerPosition = straight.calculateEndPosition().add(
                    fromStraightEndToCircleCenter);
            curve.setPosition(centerPosition);

            Vector relativeStartPoint = fromStraightEndToCircleCenter
                    .multiply(-1);

            curve.setRelativeStartPos(relativeStartPoint);

            Vector relativeEndPoint = curve.calculateRelativeEndPoint();
            return;
        }

        if (previous.isCurve()) {
            Curve previousCurve = (Curve) previous;

            double curveSameDirection = 1;

            if ((previousCurve.getAngle() < 0 && curve.getAngle() > 0)
                    || (previousCurve.getAngle() > 0 && curve.getAngle() < 0)) {
                curveSameDirection = -1;
            }

            Vector previousEndPoint = previousCurve.getPosition().add(
                    previousCurve.calculateRelativeEndPoint());
            Vector fromPreviousEndPointToCircleCenter = previousCurve
                    .calculateRelativeEndPoint().normalize()
                    .multiply(curve.getRadius() * curveSameDirection * -1);
            Vector center = previousEndPoint
                    .add(fromPreviousEndPointToCircleCenter);
            curve.setPosition(center);
            curve.setRelativeStartPos(fromPreviousEndPointToCircleCenter
                    .multiply(-1));
            return;

        }

    }

    // private DoublePoint appendCurve(DoublePoint currentPos,
    // TrackPieces pieceToAppend, float rotation) {
    //
    // // The angle of the current position based on the arch of the curve we
    // // are about to add
    // double angleOfStart = Math.atan2(currentPos.x, currentPos.y);
    //
    // // The end angle of the curve
    // double angleOfEnd = angleOfStart + Math.toRadians(pieceToAppend.angle);
    //
    // double radius = pieceToAppend.radius;
    // float angle = (float) Math.toRadians(pieceToAppend.angle);
    // double x = Math.cos(angle) * radius;
    // double y = Math.sin(angle) * radius;
    //
    // // endPoint for a curve that is going right (increasing x) and down
    // // (increasing y)
    // DoublePoint rotated = rotate(-90 + rotation, x, y);
    //
    // double offsetx = Math.cos(Math.toRadians(-90 - rotation)) * radius;
    // double offsety = Math.sin(Math.toRadians(-90 - rotation)) * radius;
    //
    // DoublePoint endPoint = new DoublePoint(rotated.x + currentPos.x
    // + offsetx, rotated.y + currentPos.y + offsety);
    //
    // System.out.println(endPoint);
    // return endPoint;
    //
    // }

    // private void plotPosition(Graphics g, TrackModel track,
    // PiecePosition position, DoublePoint origo, float rotation) {
    //
    // TrackPieces currentPiece = track.getPieceForIndex(position.pieceIndex);
    //
    // if (currentPiece.isCurve()) {
    // plotPosInCurve(g, currentPiece, position, origo, rotation);
    // } else {
    // plotPosInStraight(g, position, origo, rotation);
    // }
    //
    // }
    //
    // private void plotPosInStraight(Graphics g, PiecePosition position,
    // DoublePoint origo, float rotation) {
    // double x = origo.x + position.inPieceDistance;
    // double y = origo.y;
    //
    // DoublePoint rotated = rotate(rotation, origo, x, y);
    //
    // g.fillRect((int) rotated.x, (int) rotated.y, 6, 6);
    // }
    //
    // private void plotPosInCurve(Graphics g, TrackPieces piece,
    // PiecePosition position, DoublePoint origo, float rotation) {
    //
    // DoublePoint circumferencePoint = pointOnCircumference(piece,
    // position.inPieceDistance);
    // DoublePoint rotated = rotate(-90 + rotation, new DoublePoint(0, 0),
    // circumferencePoint);
    // double radius = piece.radius;
    // DoublePoint toPlot = new DoublePoint(rotated.x + origo.x, rotated.y
    // + radius + origo.y);
    //
    // g.fillRect((int) toPlot.x, (int) toPlot.y, 6, 6);
    //
    // }
    //
    // private DoublePoint pointOnCircumference(TrackPieces piece,
    // double travelledDistance) {
    //
    // double radius = piece.radius;
    // double circumference = 2 * Math.PI * radius;
    // double travelledAngle = travelledDistance / circumference * 360;
    //
    // float angle = (float) Math.toRadians(travelledAngle);
    // double x = Math.cos(angle) * radius;
    // double y = Math.sin(angle) * radius;
    //
    // return new DoublePoint(x, y);
    // }
    //
    // public DoublePoint rotate(double rotate, DoublePoint origo, double
    // xpoint,
    // double ypoint) {
    // rotate = (float) Math.toRadians((double) rotate);
    // double newX = origo.x + Math.cos(rotate) * (xpoint - origo.x)
    // - Math.sin(rotate) * (ypoint - origo.y);
    // double newY = origo.y + Math.sin(rotate) * (xpoint - origo.x)
    // + Math.cos(rotate) * (ypoint - origo.y);
    // return new DoublePoint(newX, newY);
    // }
    //
    // private DoublePoint rotate(double rotate, DoublePoint origo,
    // DoublePoint point) {
    // return rotate(rotate, origo, point.x, point.y);
    // }
    //
    // private DoublePoint rotate(double rotate, DoublePoint
    // pointOnCircumference) {
    // return rotate(rotate, new DoublePoint(0, 0), pointOnCircumference);
    // }
    //
    // private DoublePoint rotate(double rotate, double x, double y) {
    // return rotate(rotate, new DoublePoint(0, 0), x, y);
    // }
}
