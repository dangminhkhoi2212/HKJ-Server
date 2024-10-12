import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserExtras } from 'app/entities/user-extra/user-extra.reducer';
import { getEntities as getHkjJewelryModels } from 'app/entities/hkj-jewelry-model/hkj-jewelry-model.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-track-search-image.reducer';

export const HkjTrackSearchImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userExtras = useAppSelector(state => state.userExtra.entities);
  const hkjJewelryModels = useAppSelector(state => state.hkjJewelryModel.entities);
  const hkjTrackSearchImageEntity = useAppSelector(state => state.hkjTrackSearchImage.entity);
  const loading = useAppSelector(state => state.hkjTrackSearchImage.loading);
  const updating = useAppSelector(state => state.hkjTrackSearchImage.updating);
  const updateSuccess = useAppSelector(state => state.hkjTrackSearchImage.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-track-search-image${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserExtras({}));
    dispatch(getHkjJewelryModels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.searchOrder !== undefined && typeof values.searchOrder !== 'number') {
      values.searchOrder = Number(values.searchOrder);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjTrackSearchImageEntity,
      ...values,
      user: userExtras.find(it => it.id.toString() === values.user?.toString()),
      jewelry: hkjJewelryModels.find(it => it.id.toString() === values.jewelry?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...hkjTrackSearchImageEntity,
          createdDate: convertDateTimeFromServer(hkjTrackSearchImageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjTrackSearchImageEntity.lastModifiedDate),
          user: hkjTrackSearchImageEntity?.user?.id,
          jewelry: hkjTrackSearchImageEntity?.jewelry?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjTrackSearchImage.home.createOrEditLabel" data-cy="HkjTrackSearchImageCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjTrackSearchImage.home.createOrEditLabel">Create or edit a HkjTrackSearchImage</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="hkj-track-search-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjTrackSearchImage.searchOrder')}
                id="hkj-track-search-image-searchOrder"
                name="searchOrder"
                data-cy="searchOrder"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTrackSearchImage.createdBy')}
                id="hkj-track-search-image-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTrackSearchImage.createdDate')}
                id="hkj-track-search-image-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjTrackSearchImage.lastModifiedBy')}
                id="hkj-track-search-image-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTrackSearchImage.lastModifiedDate')}
                id="hkj-track-search-image-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-track-search-image-user"
                name="user"
                data-cy="user"
                label={translate('serverApp.hkjTrackSearchImage.user')}
                type="select"
              >
                <option value="" key="0" />
                {userExtras
                  ? userExtras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-track-search-image-jewelry"
                name="jewelry"
                data-cy="jewelry"
                label={translate('serverApp.hkjTrackSearchImage.jewelry')}
                type="select"
              >
                <option value="" key="0" />
                {hkjJewelryModels
                  ? hkjJewelryModels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-track-search-image" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default HkjTrackSearchImageUpdate;
