$jq(document).ready(function() {
	$jq('form').each(function() {
		setSubmitForForm(this, formValid(this));
	})
	
	if (typeof formValid !== 'undefined') {
		$jq('input[type="text"], input[type="password"], input[type="text"]').keyup(function() {
			var form = this.closest("form");
			setSubmitForForm(form, formValid(form));
	     });
		
		$jq(':input').change(function() {
			var form = this.closest("form");
			setSubmitForForm(form, formValid(form));
	     });
	}
});

function setSubmitForForm(form, enable) {
	if(enable) {
		$jq(form).find(':input[type="submit"]').prop('disabled', false);
    } else {
    	$jq(form).find(':input[type="submit"]').prop('disabled', true);
    }
}