/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import {FieldArray, Formik} from 'formik';
import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {Button} from '../../../../../common/components';
import Layout from '../../../../../common/containers/setup-forms/Layout';
import getInitialGenerateNewDXPKey from '../../../../../common/utils/constants/getInitialGenerateNewDXPKey';
import AdminInputs from '../AdminInputs';
import GenerateCardLayout from '../GenerateCardLayout';

const RequiredInformation = ({
	errors,
	handleComeBackPage,
	handlePage,
	infoSelectedKey,
	touched,
	values,
}) => {
	const [baseButtonDisabled, setBaseButtonDisabled] = useState(true);
	const [addButtonDisabled, setAddButtonDisabled] = useState(false);

	const [availableAdminsRoles, setAvailableAdminsRoles] = useState(1);

	const hasTouched = !Object.keys(touched).length;
	const hasError = Object.keys(errors).length;

	const getAvaliableKeys =
		Number(
			infoSelectedKey.getSelectedSubscription?.keyActivationAvailable.split(
				' '
			)[0]
		) + Number(values?.keys.length);

	const getAvaliableKeys1 = Number(
		infoSelectedKey.getSelectedSubscription?.keyActivationAvailable.split(
			' '
		)[2]
	);

	useEffect(() => {
		setBaseButtonDisabled(hasTouched || hasError);
		setAddButtonDisabled(getAvaliableKeys === getAvaliableKeys1);
	}, [getAvaliableKeys, getAvaliableKeys1, hasError, hasTouched]);

	return (
		<div className="d-flex justify-content-end">
			<Layout
				footerProps={{
					footerClass: 'mx-5 mb-2',
					leftButton: (
						<Link to={handleComeBackPage}>
							<Button
								className="btn btn-borderless btn-style-neutral"
								displayType="secondary"
							>
								Cancel
							</Button>
						</Link>
					),
					rightButton: (
						<div>
							<Button
								className="btn btn-secondary mr-3"
								displayType="secundary"
							>
								Previous
							</Button>

							<Button
								disabled={baseButtonDisabled}
								displayType="primary"
								onClick={handlePage}
							>
								Generate {availableAdminsRoles} Key
							</Button>
						</div>
					),
				}}
				headerProps={{
					headerClass: 'ml-5 my-4',
					helper:
						'Fill out the information required to generate the activation key',
					title: 'Generate Activation Key(s)',
				}}
				layoutType="cp-required-info"
			>
				<FieldArray
					name="dxp.admins"
					render={({pop, push}) => (
						<>
							<div className="px-6">
								<h4>Environment Details</h4>

								<div className="dropdown-divider mb-4 mt-2"></div>

								<div className="mb-2">
									<div className="mr-3 w-100">
										<label htmlFor="basicInputText">
											Environment Name
										</label>

										<ClayInput
											id="basicInputText"
											placeholder="e.g. Liferay Ecommerce Site"
											type="text"
										/>

										<h6 className="font-weight-normal mb-2 mt-1 mx-3 pb-4">
											Name this environment. This cannot
											be edited later.
										</h6>
									</div>
								</div>

								<div className="mb-2">
									<div className="mr-3 w-100">
										<label htmlFor="basicInputText">
											Description
										</label>

										<ClayInput
											id="basicInputText"
											placeholder="e.g. Liferay Dev Environment – ECOM DXP 7.2 "
											type="text"
										/>

										<h6 className="font-weight-normal mb-3 mt-1 mx-3 pb-4">
											Include a description to uniquely
											identify this environment. This
											cannot be edited later.
										</h6>
									</div>
								</div>
							</div>

							<div className="px-6">
								<h4>Activation Key Server Details</h4>

								<div className="dropdown-divider mb-4 mt-2"></div>

								{values.keys.map((index) => (
									<AdminInputs id={index} key={index} />
								))}

								{values?.keys.length > 1 && (
									<Button
										className="btn btn-secondary mb-3 mr-3 mt-4 py-2"
										displayType="secundary"
										onClick={() => {
											pop();
											setAvailableAdminsRoles(
												(previousAdmins) =>
													previousAdmins - 1
											);
											setBaseButtonDisabled(
												hasTouched || hasError
											);
										}}
									>
										<ClayIcon
											className="cp-button-icon-plus mr-2"
											symbol="hr"
										/>
										Remove Activation Key
									</Button>
								)}

								<Button
									className="btn btn-secondary mb-3 mt-4 py-2"
									disabled={addButtonDisabled}
									displayType="secundary"
									onClick={() => {
										push(
											getInitialGenerateNewDXPKey(
												values?.keys
											)
										);

										setAvailableAdminsRoles(
											(previousAdmins) =>
												previousAdmins + 1
										);
										setBaseButtonDisabled(true);
									}}
								>
									<ClayIcon
										className="cp-button-icon-plus mr-2"
										symbol="plus"
									/>
									Add Activation Key
								</Button>

								<div className="dropdown-divider"></div>
							</div>
						</>
					)}
				/>
			</Layout>

			<GenerateCardLayout infoSelectedKey={infoSelectedKey} />
		</div>
	);
};

const RequiredInformationForm = (props) => {
	return (
		<Formik
			initialValues={{
				description: '',
				environmentName: '',
				keys: [getInitialGenerateNewDXPKey()],
			}}
		>
			{(formikProps) => (
				<RequiredInformation {...props} {...formikProps} />
			)}
		</Formik>
	);
};

export default RequiredInformationForm;
