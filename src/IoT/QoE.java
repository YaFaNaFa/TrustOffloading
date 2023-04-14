package IoT;

public class QoE {
	private double QoS_A;
	private double QoS_M;
	private Thing Thingsource;
	private CloudFog cloudfogsource;

	public QoE(double qoS_A, double qoS_M, Thing thingsource,
			CloudFog cloudfogsource) {
		super();
		QoS_A = qoS_A;
		QoS_M = qoS_M;
		Thingsource = thingsource;
		this.cloudfogsource = cloudfogsource;
	}

	public double getQoS_A() {
		return QoS_A;
	}

	public void setQoS_A(double qoS_A) {
		QoS_A = qoS_A;
	}

	public double getQoS_M() {
		return QoS_M;
	}

	public void setQoS_M(double qoS_M) {
		QoS_M = qoS_M;
	}

	public Thing getThingsource() {
		return Thingsource;
	}

	public void setThingsource(Thing thingsource) {
		Thingsource = thingsource;
	}

	public CloudFog getCloudfogsource() {
		return cloudfogsource;
	}

	public void setCloudfogsource(CloudFog cloudfogsource) {
		this.cloudfogsource = cloudfogsource;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
